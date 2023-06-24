package at.fhj.ima.facilitron

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.repository.DepartmentRepository
import at.fhj.ima.facilitron.repository.SecurityRoleRepository
import at.fhj.ima.facilitron.service.DepartmentService
import at.fhj.ima.facilitron.service.EmployeeService
import at.fhj.ima.facilitron.service.OcupationService
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
	fun init(securityRoleService: SecurityRoleService, employeeService: EmployeeService, ocupationService: OcupationService, departmentRepository: DepartmentRepository, securityRoleRepository: SecurityRoleRepository) = CommandLineRunner{

		// ----- ROLES -----
		securityRoleService.saveRole("EMPLOYEE")
		securityRoleService.saveRole("HR")
		securityRoleService.saveRole("SUPPORT")
		securityRoleService.saveRole("SECTIONMANAGER")
		securityRoleService.saveRole("ADMIN")
		// TODO initialise all available roles (DEV ONLY)

		// ----- EMPLOYEES -----
		employeeService.saveEmployee(Employee(firstName = "Max", secondName = "Mustermann", mail="max.mustermann@mustermail.com",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("testpassword"), birthday = LocalDate.now(), roles = mutableSetOf(securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.VOLUNTARY))
		employeeService.saveEmployee(Employee(firstName = "Timo", secondName = "Kappel", mail="tk@inc.com",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("1234"), birthday = LocalDate.now(), roles = mutableSetOf(securityRoleRepository.findByName("ADMIN"), securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.FULLTIME))
		employeeService.saveEmployee(Employee(firstName = "Susi", secondName = "Unbesorgt", mail="test@idk.com",gender=Gender.FEMALE, password = BCryptPasswordEncoder().encode("1234"), birthday = LocalDate.now(), roles = mutableSetOf(securityRoleRepository.findByName("HR"), securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.PARTTIME))

		// Occupations
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(1)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(1), workload = "Something"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(2)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(2), workload = "Security Audits"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(2)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(2), workload = "Bug Fixes"))

		// Departments
		departmentRepository.save(Department(name = "Business", description = "taking your money...", head = employeeService.getEmployeeById(1)))
		departmentRepository.save(Department(name = "IT Services", description = "helping business", head = employeeService.getEmployeeById(1)))
		departmentRepository.save(Department(name = "Development", description = "finding new ways to take your money", head = employeeService.getEmployeeById(1)))
		departmentRepository.save(Department(name = "Accounting", description = "calculating how much money we took", head = employeeService.getEmployeeById(1)))
	}

}

fun main(args: Array<String>) {
	runApplication<FacilitronApplication>(*args)
}
