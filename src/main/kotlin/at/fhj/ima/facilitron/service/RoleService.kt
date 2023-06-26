package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.SecurityRole
import at.fhj.ima.facilitron.repository.SecurityRoleRepository
import org.springframework.stereotype.Service

@Service
class RoleService (
    var securityRoleRepository: SecurityRoleRepository
){
    fun getAllRoles():List<SecurityRole> {
        return securityRoleRepository.findAll().iterator().asSequence().toList()
    }

    fun getRolesByName(roles: List<String>):MutableSet<SecurityRole> {
        val out = mutableSetOf<SecurityRole>()
        roles.forEach {
            out.add(securityRoleRepository.findByName(it))
        }
        return out
    }
}