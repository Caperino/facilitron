package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.Ocupation
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.OcupationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TimeRestController (val ocupationService: OcupationService ) {

    @PostMapping(DefaultURL.API_SAVE_TIME)
    fun saveTime(@RequestBody ocupation: Ocupation) = ocupationService.saveOcupation(ocupation)

}