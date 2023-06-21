package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.Ocupation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OcupationRepository : CrudRepository<Ocupation, Int> {

    fun findByEmployee_id(id: Int):List<Ocupation>
}