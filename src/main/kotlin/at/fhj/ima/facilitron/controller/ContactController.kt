package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.ContactService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ContactController (
    val contactService: ContactService
) {
    @PostMapping(DefaultURL.CONTACT_URL)
    fun newContact(
        model: Model,
        @RequestParam name:String,
        @RequestParam email:String,
        @RequestParam message:String
    ):String{
        val sent = contactService.sendEmail(name, email, message)
        model.addAttribute("Contact_Message", sent)
        return "index"
    }
}