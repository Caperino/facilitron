package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.security.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Controller
class AuthenticationController(
    @Autowired val service: AuthenticationService,
    @Autowired val internalCookieService: InternalCookieService
){

    @PostMapping(DefaultURL.LOGIN_PAGE_URL)
    fun newLogin(
        model: Model,
        req:HttpServletRequest,
        resp:HttpServletResponse,
        @RequestParam mail:String,
        @RequestParam password:String
    ):String{
        val authResponse = service.authenticate(AuthenticationRequest(mail, password))

        // Validation checking
        if (authResponse.exception != null){

            // ----- Logging -----
            println("ERROR AUTHENTICATION - redirecting to login page")
            // ----- /Logging -----

            resp.status = 401
            return DefaultView.LOGIN_VIEW
        }

        val authCookie = internalCookieService.generateAuthCookie(authResponse.token)

        // add authentication cookie
        resp.addCookie(authCookie)
        resp.sendRedirect(DefaultURL.AUTHENTICATED_LANDING_URL)

        // ----- Logging -----
        println("successful authentication, redirect set")
        // ----- /Logging -----

        return DefaultView.REDIRECTOR
    }

    @PostMapping(DefaultURL.REGISTER_PAGE_URL)
    fun registerHandler(
        //@RequestBody request: UnsafeRegisterRequest,
        req: HttpServletRequest,
        resp: HttpServletResponse,
        model:Model,
        @RequestParam firstname:String?,
        @RequestParam secondname:String?,
        @RequestParam mail:String?,
        @RequestParam password:String?,
        @RequestParam phone:String?
    ): String {
        val unsafeRegisterRequest = UnsafeRegisterRequest(firstname, secondname, mail, password, phone)

        if (!unsafeRegisterRequest.evaluateState()){
            model.addAttribute("error", RegisterResponse(exception = SecurityException.MISSINGVALUES))
            model.addAttribute("registerData", unsafeRegisterRequest)

            // ----- Logging -----
            println("ERROR REGISTERING - incomplete parameter list")
            // ----- /Logging -----

            return DefaultView.REGISTER_VIEW
        }

        val regResponse = service.register(RegisterRequest(
            unsafeRegisterRequest.firstname!!,
            unsafeRegisterRequest.secondname!!,
            unsafeRegisterRequest.mail!!,
            unsafeRegisterRequest.password!!,
            unsafeRegisterRequest.phone)
        )

        if (regResponse.exception != null){
            model.addAttribute("error", regResponse.exception)
            model.addAttribute("registerData", unsafeRegisterRequest)

            // ----- Logging -----
            println("ERROR REGISTERING - leading back to register page")
            // ----- /Logging -----

            return DefaultView.REGISTER_VIEW
        } else {
            // ----- Logging -----
            println("successful registering, redirect set")
            // ----- /Logging -----

            resp.sendRedirect(DefaultURL.POST_REGISTER_URL)
        }
        return DefaultView.REDIRECTOR
    }

    @PostMapping("/auth/logout")
    fun logout(
        req:HttpServletRequest,
        resp:HttpServletResponse
    ):String{
        // delete currently active cookie
        resp.addCookie(internalCookieService.deleteAuthCookie())

        // redirect back to public page
        resp.sendRedirect(DefaultURL.PUBLIC_LANDING_URL)

        // ----- Logging -----
        println("successful logout")
        // ----- /Logging -----

        return DefaultView.REDIRECTOR
    }

}