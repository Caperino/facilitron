package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.SecurityRole
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SecurityRoleRepository : CrudRepository<SecurityRole, Int> {

    fun findByName(name:String):SecurityRole

}