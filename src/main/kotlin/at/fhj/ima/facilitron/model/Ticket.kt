package at.fhj.ima.facilitron.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Base Class for all tickets
 * @author SJB
 * @param id is AutoGenerated
 * @param priority is the priority of the ticket
 * @param category is the category of the ticket
 * @param subject is the subject of the ticket
 * @param description is the description of the ticket
 * @param opened is the time the ticket was opened
 * @param openedBy is the employee that opened the ticket
 * @param closed is the time the ticket was closed
 * @param closedBy is the employee that closed the ticket
 * @param ticketStatus is the current status of the ticket
 */
@Entity
class Ticket (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val priority:Priority = Priority.LOW,
    @ManyToOne(fetch = FetchType.EAGER)
    val category: Category,
    val subject: String,
    val description:String? = null,
    val opened:LocalDateTime = LocalDateTime.now(),
    @ManyToOne(fetch = FetchType.EAGER)
    val openedBy: Employee,
    var closed:LocalDateTime? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    var closedBy: Employee? = null,
    var ticketStatus: TicketStatus = TicketStatus.OPEN
) {
    fun isClosed():Boolean {
        return ticketStatus == TicketStatus.CLOSED;
    }
    fun isWaiting():Boolean {
        return ticketStatus == TicketStatus.WAITING;
    }

    fun getopenedTimeSeconds(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return opened.format(formatter)
    }

    fun getclosedTimeSeconds(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return closed!!.format(formatter)
    }
}