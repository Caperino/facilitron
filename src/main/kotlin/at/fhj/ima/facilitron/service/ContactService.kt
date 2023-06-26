package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.security.Defaults.CONTACT_EMAIL
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service

@Service
class ContactService(
    private val mailSender: JavaMailSenderImpl
) {

    fun sendEmail(name: String, email: String, message: String):String {
        try {
            val mailContext = SimpleMailMessage();
            mailContext.setTo(CONTACT_EMAIL)
            mailContext.subject = "New Contact request from: $name + $email"
            mailContext.text = message
            mailSender.send(mailContext)
            return "Successful"
        } catch (me: MailException) {
            return "Error when sending mail!"
        }
    }
}