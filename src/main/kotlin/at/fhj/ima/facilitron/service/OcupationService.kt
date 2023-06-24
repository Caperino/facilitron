package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.Employee
import at.fhj.ima.facilitron.model.Ocupation
import at.fhj.ima.facilitron.model.OcupationType
import at.fhj.ima.facilitron.repository.OcupationRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class OcupationService (
    val ocupationRepository: OcupationRepository
) {

    fun saveOcupation(oc: Ocupation) {
        if (oc.type == OcupationType.DEPATURE) {
            val tmp = ocupationRepository.getOcupationByDepartureTimeIsNullAndEmployee(oc.employee)
            tmp.departureTime = LocalDateTime.now()
            tmp.workload = oc.workload
            tmp.type = oc.type
            ocupationRepository.save(tmp)
        }
        if (oc.type == OcupationType.ARRIVAL) {
            ocupationRepository.save(oc)
        }
    }

    fun getOcuptationOfEmployee(employee: Employee):List<Ocupation> {
        return ocupationRepository.getOcupationsByEmployee(employee)
    }
}