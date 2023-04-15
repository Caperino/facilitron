package at.fhj.ima.facilitron

import at.fhj.ima.facilitron.service.SecurityRoleService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FacilitronApplication{

	@Bean
	fun init(securityRoleService: SecurityRoleService) = CommandLineRunner{

		// ----- ROLES -----
		securityRoleService.saveRole("EMPLOYEE")
		securityRoleService.saveRole("HR")
		securityRoleService.saveRole("SUPPORT")
		securityRoleService.saveRole("SECTIONMANAGER")
		securityRoleService.saveRole("ADMIN")
		// TODO initialise all available roles (DEV ONLY)

		// ----- EMPLOYEES -----

	}

}

fun main(args: Array<String>) {
	runApplication<FacilitronApplication>(*args)
}
