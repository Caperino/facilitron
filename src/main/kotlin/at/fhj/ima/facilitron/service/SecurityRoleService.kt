package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.SecurityRole
import at.fhj.ima.facilitron.repository.SecurityRoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SecurityRoleService(
    @Autowired val securityRoleRepository: SecurityRoleRepository
) {

    /**
     * for initially adding available roles to the system
     * @param name the given name of the role
     * @author TK Inc.
     */
    fun saveRole(name:String){
        securityRoleRepository.save(SecurityRole(null, name = name))
    }

}