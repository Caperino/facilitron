package at.fhj.ima.facilitron

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.repository.CategoryRepository
import at.fhj.ima.facilitron.repository.DepartmentRepository
import at.fhj.ima.facilitron.repository.SecurityRoleRepository
import at.fhj.ima.facilitron.repository.TicketRepository
import at.fhj.ima.facilitron.service.*
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
	fun init(securityRoleService: SecurityRoleService, employeeService: EmployeeService, ocupationService: OcupationService,
			 departmentRepository: DepartmentRepository, securityRoleRepository: SecurityRoleRepository,
			 ticketRepository : TicketRepository, categoryRepository : CategoryRepository,
			 ticketCommentService : TicketCommentService) = CommandLineRunner{

		// ----- ROLES -----
		securityRoleService.saveRole("EMPLOYEE")
		securityRoleService.saveRole("HR")
		securityRoleService.saveRole("SUPPORT")
		securityRoleService.saveRole("SECTIONMANAGER")
		securityRoleService.saveRole("ADMIN")

		// Departments
		departmentRepository.save(Department(name = "Marketing", description = "Focuses on promoting the companys products or services, conducting market research, managing advertising campaigns, branding, and public relations.", head = null/*head = employeeService.getEmployeeById(1)*/))
		departmentRepository.save(Department(name = "IT Services", description = "Providing seamless technology solutions, supporting productivity and empowering the organization through efficient and innovative IT services.", head = null/*head = employeeService.getEmployeeById(1)*/))
		departmentRepository.save(Department(name = "Operations", description = "Oversees the day-to-day activities of the company, including production, quality control, and process improvement to ensure smooth and efficient operations.", head = null/*head = employeeService.getEmployeeById(2)*/))
		departmentRepository.save(Department(name = "Finance and Accounting", description = "Handles the financial aspects of the company, such as budgeting, financial planning, bookkeeping, payroll, tax compliance, and financial reporting.", head = null/*head = employeeService.getEmployeeById(3)*/))
		departmentRepository.save(Department(name = "Human Resources", description = "Responsible for managing the companys personnel, including recruitment, training, performance evaluations, and ensuring compliance with employment laws.", head = null/*head = employeeService.getEmployeeById(3)*/))

		// ----- EMPLOYEES -----
		employeeService.saveEmployee(Employee(firstName = "Max", secondName = "Mustermann", mail="max.mustermann@facilitron.org",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("Max1234"), birthday = LocalDate.parse("1990-06-10"), roles = mutableSetOf(securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.VOLUNTARY, department = departmentRepository.findById(1).get(), entryDate = LocalDate.parse("2023-01-01")))
		employeeService.saveEmployee(Employee(firstName = "Timo", secondName = "Kappel", mail="timo.kappel@facilitron.org",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("Timo1234"), birthday = LocalDate.parse("2003-07-14"), roles = mutableSetOf(securityRoleRepository.findByName("ADMIN"), securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.FULLTIME, department = departmentRepository.findById(2).get(), entryDate = LocalDate.parse("2023-04-01")))
		employeeService.saveEmployee(Employee(firstName = "Huba", secondName = "Csicsics", mail="huba.csicsics@facilitron.org",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("Huba1234"), birthday = LocalDate.parse("2002-05-05"), roles = mutableSetOf(securityRoleRepository.findByName("SUPPORT"), securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.FULLTIME, department = departmentRepository.findById(4).get(), entryDate = LocalDate.parse("2023-04-01")))
		employeeService.saveEmployee(Employee(firstName = "Stefan", secondName = "Jöbstl", mail="stefan.joebstl@facilitron.org",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("Stefan1234"), birthday = LocalDate.parse("2001-08-30"), roles = mutableSetOf(securityRoleRepository.findByName("ADMIN"), securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.FULLTIME, department = departmentRepository.findById(3).get(), entryDate = LocalDate.parse("2023-04-01")))
		employeeService.saveEmployee(Employee(firstName = "Alex", secondName = "Wolf", mail="alex.wolf@facilitron.org",gender=Gender.MALE, password = BCryptPasswordEncoder().encode("Alex1234"), birthday = LocalDate.parse("2002-03-02"), roles = mutableSetOf(securityRoleRepository.findByName("HR"), securityRoleRepository.findByName("EMPLOYEE")), workingType = WorkingType.FULLTIME, department = departmentRepository.findById(5).get(), entryDate = LocalDate.parse("2023-04-01")))

		// Occupations
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(1)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(1), workload = "Projectwork: Facilitron"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(1)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(1), workload = "Project ROOTNAVIGATOR deployment"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(1)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(1), workload = "Projectwork: Facilitron"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(1)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(1), workload = "Bug Fixes"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(2)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(2), workload = "Security Audits"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(2)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(2), workload = "Bug Fixes"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(2)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(2), workload = "Security Improvement"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(2)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(2), workload = "Bug Fixes"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(3)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(3), workload = "Projectwork: Facilitron"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(3)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(3), workload = "Project ROOTNAVIGATOR deployment"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(3)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(3), workload = "Projectwork: Facilitron"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(3)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(3), workload = "Bug Fixes"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(4)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(4), workload = "Security Audits"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(4)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(4), workload = "Bug Fixes"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(4)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(4), workload = "Security Improvement"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(4)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(4), workload = "Bug Fixes"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(5)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(5), workload = "Projectwork: Facilitron Frontend"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(5)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(5), workload = "Project ROOTNAVIGATOR deployment"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(5)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(5), workload = "Projectwork: Facilitron Frontend"))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.ARRIVAL, employee = employeeService.getEmployeeById(5)))
		ocupationService.saveOcupation(Ocupation(type=OcupationType.DEPATURE, employee = employeeService.getEmployeeById(5), workload = "Bug Fixes"))

		// Category
		categoryRepository.save(Category(name="Hardware", description = "Hardware like printers, computers, servers, etc."))
		categoryRepository.save(Category(name="Software", description = "Software like SAP, Office, etc."))
		categoryRepository.save(Category(name="Diverse", description = "Everything else."))

		// Ticket
		ticketRepository.save(Ticket(subject = "Printer not working", description = "Printer is having paper jam.",
			category = categoryRepository.findCategoryByName("Hardware"), openedBy = employeeService.getEmployeeById(5),
			priority = Priority.MEDIUM))
		ticketRepository.save(Ticket(subject = "Server down", description = "Main Cloud SAP Server is not responding. Please check ASAP.",
			category = categoryRepository.findCategoryByName("Software"), openedBy = employeeService.getEmployeeById(2),
			priority = Priority.DISASTER))
		ticketRepository.save(Ticket(subject = "Coffeemachine broken", description = "Coffee machine grinding mechanism is broken. Please fix ASAP.",
			category = categoryRepository.findCategoryByName("Diverse"), openedBy = employeeService.getEmployeeById(1),
			priority = Priority.HIGH))
		ticketRepository.save(Ticket(subject = "Frontend Link Missing", description = "The link to a form is missing on the website.",
			category = categoryRepository.findCategoryByName("Software"), openedBy = employeeService.getEmployeeById(4),
			priority = Priority.MEDIUM))
		ticketRepository.save(Ticket(subject = "Out of Paper", description = "We don't have any paper left in the office. Please refill.",
			category = categoryRepository.findCategoryByName("Diverse"), openedBy = employeeService.getEmployeeById(4),
			priority = Priority.LOW))

		// Comments
		ticketCommentService.addTicketComment(TicketComment(comment = "Welcome to the Tech Support.\n" +
				"We are investigating this issue. Please be patient.", ticket = ticketRepository.findById(1).get(),
			commenter = employeeService.getEmployeeById(3)))
		ticketCommentService.addTicketComment(TicketComment(comment = "Hello, thank you for your response.\n" +
				"Let me know if you need any other Information.", ticket = ticketRepository.findById(1).get(),
			commenter = employeeService.getEmployeeById(5)))
		ticketCommentService.addTicketComment(TicketComment(comment = "Welcome to the Tech Support.\n" +
				"We are investigating this issue. Please be patient.", ticket = ticketRepository.findById(2).get(),
			commenter = employeeService.getEmployeeById(3)))
		ticketCommentService.addTicketComment(TicketComment(comment = "Welcome to the Tech Support.\n" +
				"We are investigating this issue. Please be patient.", ticket = ticketRepository.findById(3).get(),
			commenter = employeeService.getEmployeeById(3)))
		ticketCommentService.addTicketComment(TicketComment(comment = "Welcome to the Tech Support.\n" +
				"We are investigating this issue. Please be patient.", ticket = ticketRepository.findById(4).get(),
			commenter = employeeService.getEmployeeById(3)))
		ticketCommentService.addTicketComment(TicketComment(comment = "Welcome to the Tech Support.\n" +
				"We are investigating this issue. Please be patient.", ticket = ticketRepository.findById(5).get(),
			commenter = employeeService.getEmployeeById(3)))
		ticketCommentService.addTicketComment(TicketComment(comment = "Hello, thank you for your response.\n" +
				"Let me know if you need any other Information.", ticket = ticketRepository.findById(5).get(),
			commenter = employeeService.getEmployeeById(4)))

	}

}

fun main(args: Array<String>) {
	runApplication<FacilitronApplication>(*args)
}
