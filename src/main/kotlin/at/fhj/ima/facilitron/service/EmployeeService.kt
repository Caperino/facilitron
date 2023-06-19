package at.fhj.ima.facilitron.service

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
}