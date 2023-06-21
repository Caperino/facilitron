package at.fhj.ima.facilitron.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity
class Ocupation (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val type:String? = null,
    val time:LocalDate = LocalDate.now(),
    val workload:String? = null
) {
}