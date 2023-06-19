package at.fhj.ima.facilitron.model

import jakarta.persistence.*
import java.time.LocalDate

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
 * @param comments is the chat history
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
    val opened:LocalDate = LocalDate.now(),
    @ManyToOne(fetch = FetchType.EAGER)
    val openedBy: Employee,
    val closed:LocalDate? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    val closedBy: Employee?,
    val ticketStatus: TicketStatus = TicketStatus.OPEN,
    @OneToMany(fetch = FetchType.EAGER)
    val comments:MutableSet<TicketComment> = mutableSetOf(),
) {
    fun isClosed():Boolean {
        return ticketStatus == TicketStatus.CLOSED;
    }
    fun isWaiting():Boolean {
        return ticketStatus == TicketStatus.WAITING;
    }
}