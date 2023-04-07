package at.fhj.ima.facilitron.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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
    @Autowired val jwtService:JwtService,
    @Autowired val userDetailsService: UserDetailsService
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
            request.requestURI == DefaultURL.PUBLIC_TEMP_TESTING
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

            response.sendRedirect(DefaultURL.LOGIN_PAGE_URL)
            return
        }


        if (!fromCookie && !authHeader.startsWith("Bearer ")){
            println("invalid token format")

            response.sendRedirect(DefaultURL.LOGIN_PAGE_URL)
            return
        }

        // 7 because of Bearer
        val jwt:String = if (!fromCookie){
            authHeader.substring(7)
        } else {
            authHeader
        }
        val userMail:String

        try {
            userMail = jwtService.extractUsermail(jwt)
        }
        catch (e: Exception){
            println("token examination failed")
            println(e.message)

            response.sendRedirect(DefaultURL.LOGIN_PAGE_URL)
            return
        }

        if (SecurityContextHolder.getContext().authentication == null){
            val userDetails = userDetailsService.loadUserByUsername(userMail)
            if (jwtService.isTokenValid(jwt, userDetails)){
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}