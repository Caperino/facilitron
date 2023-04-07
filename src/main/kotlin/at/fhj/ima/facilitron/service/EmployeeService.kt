package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.repository.EmployeeRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class EmployeeService(
    @Autowired val employeeRepository:EmployeeRepository
) {
    // TODO implement all functionality regarding database
}