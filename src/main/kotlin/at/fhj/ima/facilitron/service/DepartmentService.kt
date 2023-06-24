package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.Department
import at.fhj.ima.facilitron.repository.DepartmentRepository
import org.springframework.stereotype.Service

@Service
class DepartmentService (
    val departmentRepository: DepartmentRepository
) {
    fun getAllDepartments():List<Department>{
        return departmentRepository.findAll().iterator().asSequence().toList()
    }

    fun saveDepartment(department: Department) {
        departmentRepository.save(department)
    }
}