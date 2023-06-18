package at.fhj.ima.facilitron.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Gender(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val name:String
)