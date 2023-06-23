package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.repository.TicketRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class TicketService (
    val ticketRepository: TicketRepository
) {
    fun getAllTickets():List<Ticket> {
        return ticketRepository.findAll().iterator().asSequence().toList()
    }

    fun getTicketDetails(id: Int): Ticket {
        return ticketRepository.findTicketById(id)!!
    }

    fun searchTickets(cats: List<Category>, search: String):List<Ticket> {
        val returnList : MutableList<Ticket> = mutableListOf()
        cats.forEach {
            returnList.addAll(ticketRepository.findTicketsBySubjectContainingIgnoreCaseOrCategory(search, it))
        }
        return returnList.toList()
    }

    /*fun getTicketCommentsByTicketId(id: Int):List<TicketComment> {

    }*/

    fun closeTicket(tk: Ticket, employee: Employee):Boolean {
        try {
            tk.closed = LocalDateTime.now()
            tk.closedBy = employee;
            tk.ticketStatus = TicketStatus.CLOSED
            ticketRepository.save(tk)
            return true;
        } catch (e:Exception) {
            return false;
        }
    }

    fun openTicket(tk: Ticket) {
        ticketRepository.save(tk)
    }
}