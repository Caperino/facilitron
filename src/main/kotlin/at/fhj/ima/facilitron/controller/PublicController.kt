package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.security.DefaultURL
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
    fun hiddenPage(model: Model):String{
        return "hidden"
    }

    @GetMapping(DefaultURL.PUBLIC_TEMP_TESTING)
    fun newPublicPage(model: Model):String{
        return "public"
    }
}