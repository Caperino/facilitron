package at.fhj.ima.facilitron.model

import jakarta.persistence.*

/**
 * Base Class for all categories
 * @author SJB
 * @param id is AutoGenerated.
 * @param name is the name of the category
 * @param description is the description of the category
 */
@Entity
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val name:String,
    val description:String? = null
)