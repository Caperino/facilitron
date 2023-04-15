package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.security.DefaultClaim
import at.fhj.ima.facilitron.security.DefaultURL
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/")
class PublicController{
    @GetMapping(DefaultURL.PUBLIC_LANDING_URL)
    fun publicPage(
        model: Model,
        @RequestParam or:String?
    ):String{
        return "index"
    }

    @GetMapping("/hidden")
    fun hiddenPage(
        model: Model,
        req: HttpServletRequest,
        resp: HttpServletResponse
    ):String{
        // add default personal details inside JWT to model
        // ONLY VALID FOR AUTHENTICATED RESOURCES
        DefaultClaim.claimSet.forEach { model.addAttribute(req.getAttribute(it))  }

        return "hidden"
    }

    @GetMapping("/hidden-admin")
    fun hiddenAdminPage(model: Model):String{
        return "hiddenadmin"
    }

    @GetMapping(DefaultURL.PUBLIC_TEMP_TESTING)
    fun newPublicPage(model: Model):String{
        return "public"
    }
}