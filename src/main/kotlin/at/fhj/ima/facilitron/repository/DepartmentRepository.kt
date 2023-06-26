package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.Department
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : CrudRepository<Department, Int> {

    fun getDepartmentByNameContainingIgnoreCase(name: String):Department

}