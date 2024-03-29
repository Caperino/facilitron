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

    fun getCategoryByName(search: String):Category {
        return categoryRepository.findCategoryByName(search)
    }

    fun getAllCategories():List<Category> {
        return categoryRepository.findAll().iterator().asSequence().toList()
    }

    fun saveCategory(category: Category) {
        categoryRepository.save(category)
    }
}