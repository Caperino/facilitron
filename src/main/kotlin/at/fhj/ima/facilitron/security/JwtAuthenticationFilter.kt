package at.fhj.ima.facilitron.security

import at.fhj.ima.facilitron.model.JwtUserDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import kotlin.text.Typography.section

@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter(
    val jwtService:JwtService,
    val userDetailsService: UserDetailsService,
    val internalCookieService: InternalCookieService
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // TODO --> exclude public pages from this filter
        // skip if public path
        if (request.requestURI.startsWith(DefaultURL.AUTHENTICATION_PREFIX) ||
            request.requestURI == DefaultURL.PUBLIC_LANDING_URL ||
            request.requestURI == DefaultURL.PUBLIC_TEMP_TESTING ||
            request.requestURI.startsWith(DefaultURL.STATIC_PREFIX)
            ){
            filterChain.doFilter(request, response)
            return
        }

        val authCookieContent :String? = request.cookies?.find { it.name == "auth" }?.value
        val authHeaderContent :String? = request.getHeader("Authorization")
        var fromCookie:Boolean = false
        val authHeader : String = if (authCookieContent != null) {
            fromCookie = true
            authCookieContent
        } else if (authHeaderContent != null){
            authHeaderContent
        } else {
            println("no authentication token found")

            response.sendRedirect(DefaultURL.PUBLIC_LANDING_URL + "#Login")
            return
        }


        if (!fromCookie && !authHeader.startsWith("Bearer ")){
            println("invalid token format")

            response.addCookie(internalCookieService.deleteAuthCookie())
            response.sendRedirect(DefaultURL.PUBLIC_LANDING_URL + "#Login")
            return
        }

        // 7 because of Bearer
        val jwt:String = if (!fromCookie){
            authHeader.substring(7)
        } else {
            authHeader
        }

        // employee information container
        val userInformation : JwtUserDetails
        val newAuthCookie:Cookie

        if (SecurityContextHolder.getContext().authentication == null){
            val tokenState:Boolean

            try {
                tokenState = jwtService.isTokenValid(token = jwt)
            }catch(e : Exception){
                println("token fully expired")
                response.addCookie(internalCookieService.deleteAuthCookie())
                response.sendRedirect(DefaultURL.PUBLIC_LANDING_URL + "#Login")
                return
            }

            if (tokenState){
                try {
                    val information = jwtService.extractPersonalDetails(jwt)
                    println(information["roles"])
                    val authorities = information["roles"]?.split(";")!!
                    userInformation = JwtUserDetails(
                        information["mail"]!!,
                        information["firstName"]!!,
                        information["secondName"]!!,
                        if (authorities.contains("")) listOf() else authorities,
                        information["id"]!!
                    )
                }catch(e:Exception){
                    println("----- - EXCEPTION FILTER - -----")
                    println("invalid token")
                    println("----- EXCEPTION FILTER END -----")

                    response.addCookie(internalCookieService.deleteAuthCookie())
                    response.sendRedirect(DefaultURL.PUBLIC_LANDING_URL + "#Login")
                    return
                }
            } else if (!jwtService.allowTokenExtension(token = jwt)){
                response.addCookie(internalCookieService.deleteAuthCookie())
                response.sendRedirect(DefaultURL.PUBLIC_LANDING_URL + "#Login")
                return
            } else
            {
                println("token subject to extension")
                val newAuthToken = jwtService.extendToken(token = jwt)
                newAuthCookie = internalCookieService.generateAuthCookie(newAuthToken)
                response.addCookie(newAuthCookie)
                println("token extended")

                // token validation
                try {
                    val information = jwtService.extractPersonalDetails(newAuthCookie.value)
                    println(information["roles"])
                    val authorities = information["roles"]?.split(";")!!
                    userInformation = JwtUserDetails(
                        information["mail"]!!,
                        information["firstName"]!!,
                        information["secondName"]!!,
                        if (authorities.contains("")) listOf() else authorities,
                        information["id"]!!
                    )
                } catch (e: Exception) {
                    println("----- - EXCEPTION FILTER - -----")
                    println("invalid token")
                    println("----- EXCEPTION FILTER END -----")
                    response.sendRedirect(DefaultURL.PUBLIC_LANDING_URL + "#Login")
                    return
                }
            }

            val authToken = UsernamePasswordAuthenticationToken(
                userInformation,
                null,
                userInformation.authorities
            )
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authToken
            // ----- ----- END VALIDATION ----- -----
        } else {
            filterChain.doFilter(request, response)
            return
        }

        // providing information for visual output
        DefaultClaim.claimSet.forEach { request.setAttribute(it, userInformation[it]) }

        // resuming to standard filter chain
        filterChain.doFilter(request, response)
    }
}