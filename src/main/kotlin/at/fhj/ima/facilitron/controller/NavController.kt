package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.security.DefaultURL
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class NavController {

    @GetMapping(DefaultURL.NAVPAGE_URL)
    fun navigation(
        model: Model
    ): String {
        return "navpage"
    }
}