package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.Employee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CrudRepository<Employee, Int> {

    /**
     * gets an employee from DB for Authentication
     */
    fun findEmployeeByMail(mail:String):Employee

    fun findByMail(mail:String):Employee?

    fun findEmployeeByFirstNameContainingIgnoreCaseOrSecondNameContainingIgnoreCase(
        firstName: String,
        secondName: String
    ):List<Employee>?

    fun findEmployeeByDepartmentNameContainingIgnoreCase(departmentName: String):List<Employee>?

    fun findEmployeeById(id: Int):Employee?

    fun getEmployeeById(id: Int):Employee

}