package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.security.DefaultClaim
import at.fhj.ima.facilitron.security.DefaultURL
import dev.robinohs.totpkt.otp.totp.TotpGenerator
import dev.robinohs.totpkt.otp.totp.timesupport.generateCode
import dev.robinohs.totpkt.recovery.RecoveryCodeGenerator
import dev.robinohs.totpkt.secret.SecretGenerator
import io.github.g0dkar.qrcode.QRCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType.IMAGE_PNG_VALUE
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.util.*

@Controller
class PublicController(
    private val totpGenerator: TotpGenerator,
    private val recoveryCodeGenerator: RecoveryCodeGenerator,
    private val secretGenerator: SecretGenerator
){
    @GetMapping(DefaultURL.PUBLIC_LANDING_URL)
    fun publicPage(
        model: Model,
        @RequestParam or:String?
    ):String{
        val secret = secretGenerator.generateSecret(6)
        println(secret)
        val imageOut = ByteArrayOutputStream()
        QRCode("otpauth://totp/Facilitron?secret="+secret.secretAsString).render().writeImage(imageOut)
        val imageBytes = imageOut.toByteArray()
        val image = Base64.getEncoder().encodeToString(imageBytes);
        model.addAttribute("testCode",totpGenerator.generateCode(secret.secretAsByteArray))
        model.addAttribute("qr", image)
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