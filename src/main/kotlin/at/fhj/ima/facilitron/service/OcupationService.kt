package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.Employee
import at.fhj.ima.facilitron.model.Ocupation
import at.fhj.ima.facilitron.repository.OcupationRepository
import org.springframework.stereotype.Service

@Service
class OcupationService (
    val ocupationRepository: OcupationRepository
) {

    fun saveOcupation(oc: Ocupation) {
        ocupationRepository.save(oc)
    }
}