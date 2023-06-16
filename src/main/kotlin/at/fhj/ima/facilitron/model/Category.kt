package at.fhj.ima.facilitron.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val name:String,
    val description:String? = null
)