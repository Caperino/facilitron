package at.fhj.ima.facilitron.security

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.repository.EmployeeRepository
import at.fhj.ima.facilitron.repository.SecurityRoleRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
/**
 * special endpoints for security actions
 */
class AuthenticationService(
    @Autowired val employeeRepository: EmployeeRepository,
    @Autowired val securityRoleRepository: SecurityRoleRepository,
    @Autowired val passwordEncoder: PasswordEncoder,
    @Autowired val jwtService: JwtService,
    @Autowired val authenticationManager: AuthenticationManager
) {
    /**
     * handles internal adding of new employee
     * @param request the RegisterRequest object containing the information
     * @return AuthenticationResponse object with TOKEN or ERROR details if unsuccessful
     * @author TK Inc.
     */
    fun register(request: RegisterRequest): RegisterResponse {

        if (employeeRepository.findByMail(request.mail) != null) return RegisterResponse(exception = SecurityWarning.EMPLOYEEALREADYEXISTS)

        // building new employee
        val em = Employee(id = null, firstName = request.firstname, secondName = request.secondname, mail = request.mail,
            password = passwordEncoder.encode(request.password), phone = request.phone, gender = request.gender, birthday = request.birthday)
        employeeRepository.save(em)

        return RegisterResponse()
    }

    /**
     * handles internal authenticating of employee-login
     * @param request the RegisterRequest object containing the information
     * @return AuthenticationResponse object with TOKEN or ERROR details if unsuccessful
     * @author TK Inc.
     */
    @Throws(UsernameNotFoundException::class)
    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.mail, request.password))
        }
        catch ( e:Exception )
        {
            // ----- Logging -----
            println("Exception in authenticate:\n${e}")
            // ----- /Logging -----

            return AuthenticationResponse(token = "", SecurityWarning.FAILEDAUTHENTICATION)
        }

        // safe employee retrieval
        val em:Employee
        try {
            em = employeeRepository.findEmployeeByMail(request.mail)
        }
        catch (e:Exception){
            return AuthenticationResponse(token = "", SecurityWarning.FAILEDDATARETRIEVAL)
        }

        // TODO save extra claims
        //val jwtToken = jwtService.generateToken(em)

        val claimMap = mutableMapOf<String, String>()
        DefaultClaim.claimSet.forEach { claimMap[it] = em[it] }

        println(claimMap.toString())
        val jwtToken = jwtService.generateToken(
            subject = em.username,
            extraClaims = claimMap
        )

        return AuthenticationResponse(token = jwtToken)
    }

    /**
     * blueprint for future role assignments
     * @param mail identifies the employee
     * @author TK Inc.
     */
    fun updateRoleAssignments(mail:String, role:String){
        val em:Employee
        try {
            em = employeeRepository.findEmployeeByMail(mail = mail)
        } catch (e:Exception){
            println("exception when trying to find user - ${e.cause}")
            return
        }

        // TODO exception handling
        val newRole = securityRoleRepository.findByName(role)

        em.addAuthority(newRole)

        employeeRepository.save(em)
        return
    }
}