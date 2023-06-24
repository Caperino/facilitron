package at.fhj.ima.facilitron.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Date

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
    fun getDate():LocalDate{
        return arrivalTime.toLocalDate()
    }

    fun getArrivalTime(): LocalTime {
        return arrivalTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS)
    }

    fun getDepartureTime(): LocalTime {
        return arrivalTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS)
    }
}