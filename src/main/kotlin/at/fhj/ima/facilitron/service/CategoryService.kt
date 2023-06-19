package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.Category
import at.fhj.ima.facilitron.repository.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService (
    val categoryRepository: CategoryRepository
) {
    fun getCategoriesByName(search: String):List<Category> {
        return categoryRepository.findCategoriesByNameContainingIgnoreCase(search)
    }
}