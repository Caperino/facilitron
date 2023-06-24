package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.security.*
import at.fhj.ima.facilitron.service.StringToDate
import at.fhj.ima.facilitron.service.StringToGender
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
class AuthenticationController(
    @Autowired val service: AuthenticationService,
    @Autowired val internalCookieService: InternalCookieService
){

    @PostMapping(DefaultURL.LOGIN_PAGE_URL)
    fun loginHandler(
        model : Model,
        req : HttpServletRequest,
        resp : HttpServletResponse,
        @RequestParam mail : String,
        @RequestParam password : String
    ):String{
        val authResponse = service.authenticate(AuthenticationRequest(mail, password))

        // Validation checking
        if (authResponse.exception != null){

            // ----- Logging -----
            println("ERROR AUTHENTICATION - redirecting to login page")
            // ----- /Logging -----

            // TODO use response message in page (SEE BELOW)
            // authResponse.exception.message

            resp.status = 401
            return DefaultView.LOGIN_VIEW
        }

        val authCookie = internalCookieService.generateAuthCookie(authResponse.token)

        // add authentication cookie
        resp.addCookie(authCookie)
        resp.sendRedirect(DefaultURL.NAVPAGE_URL)

        // ----- Logging -----
        println("successful authentication, redirect set")
        // ----- /Logging -----

        return DefaultView.REDIRECTOR
    }

    @PostMapping(DefaultURL.REGISTER_PAGE_URL)
    fun registerHandler(
        req: HttpServletRequest,
        resp: HttpServletResponse,
        model: Model,
        @RequestParam firstname:String?,
        @RequestParam secondname:String?,
        @RequestParam mail:String?,
        @RequestParam password:String?,
        @RequestParam phone:String?,
        @RequestParam gender:String?,
        @RequestParam birthday: String?
    ): String {
        val unsafeRegisterRequest = UnsafeRegisterRequest(firstname, secondname, mail, password, phone, StringToGender().convert(gender?: "DIV"), StringToDate().convert(birthday?: "1.1.1970"))

        if (!unsafeRegisterRequest.evaluateState()){
            model.addAttribute("error", RegisterResponse(exception = SecurityWarning.MISSINGVALUES))
            model.addAttribute("registerData", unsafeRegisterRequest)

            // ----- Logging -----
            println("ERROR REGISTERING - incomplete parameter list")
            // ----- /Logging -----

            return "forward:"+DefaultURL.USER_CREATE
        }

        val regResponse = service.register(RegisterRequest(
            unsafeRegisterRequest.firstname!!,
            unsafeRegisterRequest.secondname!!,
            unsafeRegisterRequest.mail!!,
            unsafeRegisterRequest.password!!,
            unsafeRegisterRequest.phone,
            unsafeRegisterRequest.gender!!,
            unsafeRegisterRequest.birthday!!)
        )

        if (regResponse.exception != null){
            model.addAttribute("error", regResponse.exception)
            model.addAttribute("registerData", unsafeRegisterRequest)

            // ----- Logging -----
            println("ERROR REGISTERING - leading back to register page")
            // ----- /Logging -----

            return "forward:"+DefaultURL.USER_CREATE
        } else {
            // ----- Logging -----
            println("successful registering, redirect set")
            // ----- /Logging -----

            resp.sendRedirect(DefaultURL.USER_URL)
        }
        return DefaultView.REDIRECTOR
    }

    @PostMapping(DefaultURL.LOGOUT_PAGE_URL)
    fun logoutHandler(
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

    /**
     * NOT PRODUCTION READY blueprint for future role assignments
     * @param mail used to identify the employee
     * @param role which role should be assigned
     * @author TK Inc.
     */
    @PostMapping("/auth/addrole")
    fun addRole(
        @RequestParam mail:String,
        @RequestParam role:String
    ):ResponseEntity<String>{
        // TODO exception handling
        service.updateRoleAssignments(mail = mail, role = role)
        return ResponseEntity.ok("worked!")
    }

}