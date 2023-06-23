package at.fhj.ima.facilitron.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Ocupation (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    var type:String? = null,
    val arrivalTime: LocalDateTime = LocalDateTime.now(),
    var departureTime:LocalDateTime? = null,
    var workload:String? = null,
    @ManyToOne
    val employee: Employee
) {

}