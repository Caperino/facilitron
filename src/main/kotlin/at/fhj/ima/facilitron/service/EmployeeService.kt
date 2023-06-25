package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.AccountStatus
import at.fhj.ima.facilitron.model.Employee
import at.fhj.ima.facilitron.repository.EmployeeRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class EmployeeService(
    val employeeRepository:EmployeeRepository
) {
    fun getEmployeeById(id: Int):Employee {
        return employeeRepository.getEmployeeById(id)
    }

    fun getAllEmployees():List<Employee> {
        return employeeRepository.getAllByAccountStatusIs(AccountStatus.ACTIVE)
    }

    fun findEmployeesByName(search: String): List<Employee> {
        return employeeRepository.findEmployeeByFirstNameContainingIgnoreCaseOrSecondNameContainingIgnoreCaseOrDepartmentNameContainingIgnoreCaseAndAccountStatusIs(search, search, search, AccountStatus.ACTIVE)
    }

    fun saveEmployee(emp: Employee) {
        employeeRepository.save(emp)
    }

    fun deleteEmployee(emp: Employee) : Boolean{
        try {
            emp.accountStatus = AccountStatus.DISABLED
            employeeRepository.save(emp)
        } catch (e:Exception){
            return false
        }
        return true
    }
}