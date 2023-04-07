package at.fhj.ima.facilitron.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/")
class PublicController{
    @GetMapping("/")
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

    @GetMapping("/public")
    fun newPublicPage(model: Model):String{
        return "public"
    }
}