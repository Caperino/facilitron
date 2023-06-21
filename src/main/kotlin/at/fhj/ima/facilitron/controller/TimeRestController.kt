package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.security.DefaultURL
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TimeRestController {

    @PostMapping(DefaultURL.API_SAVE_TIME)
    fun saveTime(
        req: HttpServletRequest,
        res: HttpServletResponse
    ) {

    }

}