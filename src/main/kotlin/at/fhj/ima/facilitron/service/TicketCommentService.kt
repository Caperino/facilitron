package at.fhj.ima.facilitron.service

import at.fhj.ima.facilitron.model.Ticket
import at.fhj.ima.facilitron.model.TicketComment
import at.fhj.ima.facilitron.repository.TicketCommentRepository
import org.springframework.stereotype.Service

@Service
class TicketCommentService (
    val ticketCommentRepository: TicketCommentRepository
){

    fun findTicketCommentsByTicket(tk: Ticket):List<TicketComment> {
        return ticketCommentRepository.findTicketsCommentsByTicket(tk)
    }

    fun addTicketComment(com: TicketComment):Boolean {
        return try {
            ticketCommentRepository.save(com)
            true
        } catch (e:Exception) {
            false
        }
    }
}