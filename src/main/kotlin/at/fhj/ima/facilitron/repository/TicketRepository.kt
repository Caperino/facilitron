package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.Ticket
import at.fhj.ima.facilitron.model.TicketStatus
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : CrudRepository<Ticket, Int> {

    fun findTicketByTicketStatus(status: TicketStatus):List<Ticket>

    fun findTicketById(id: Int):Ticket?

    fun getTicketById(id: Int):Ticket

}