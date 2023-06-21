package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.File
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : CrudRepository<File, Int> {
}
