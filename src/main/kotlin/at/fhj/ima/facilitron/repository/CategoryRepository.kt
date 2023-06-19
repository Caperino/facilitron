package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.Category
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : CrudRepository<Category, Int> {

    fun findCategoriesByNameContainingIgnoreCase(name: String):List<Category>
}