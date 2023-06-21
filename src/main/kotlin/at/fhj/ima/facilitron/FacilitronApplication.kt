package at.fhj.ima.facilitron

import at.fhj.ima.facilitron.model.Employee
import at.fhj.ima.facilitron.model.Gender
import at.fhj.ima.facilitron.service.EmployeeService
import at.fhj.ima.facilitron.service.SecurityRoleService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate

@SpringBootApplication
class FacilitronApplication(private val passwordEncoder: PasswordEncoder) {

	@Bean
	fun init(securityRoleService: SecurityRoleService, employeeService: EmployeeService) = CommandLineRunner{

		// ----- ROLES -----
		securityRoleService.saveRole("EMPLOYEE")
		securityRoleService.saveRole("HR")
		securityRoleService.saveRole("SUPPORT")
		securityRoleService.saveRole("SECTIONMANAGER")
		securityRoleService.saveRole("ADMIN")
		// TODO initialise all available roles (DEV ONLY)

		// ----- EMPLOYEES -----
		employeeService.saveEmployee(Employee(firstName = "Max", secondName = "Mustermann", mail="max.mustermann@mustermail.com",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("testpassword"), birthday = LocalDate.now()))

	}

}

fun main(args: Array<String>) {
	runApplication<FacilitronApplication>(*args)
}
