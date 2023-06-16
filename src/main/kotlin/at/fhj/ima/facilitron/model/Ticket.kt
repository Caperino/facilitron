package at.fhj.ima.facilitron.model

import jakarta.persistence.*
import java.sql.Time

@Entity
class Ticket (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val priority:Priority = Priority.LOW,
    @ManyToOne(fetch = FetchType.EAGER)
    val category: Category,
    val description:String? = null,
    val opened:Time = Time(System.currentTimeMillis()),
    @ManyToOne
    val openedBy: Employee,
    @ManyToOne
    val closedBy: Employee?,
    val ticketStatus: TicketStatus = TicketStatus.OPEN,
    val closed:Time? = null,
    @OneToMany
    val comments:MutableSet<TicketComment> = mutableSetOf()
) {
    fun isClosed():Boolean {
        return ticketStatus == TicketStatus.CLOSED;
    }
    fun isWaiting():Boolean {
        return ticketStatus == TicketStatus.WAITING;
    }
}