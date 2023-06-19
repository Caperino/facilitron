package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.Category
import at.fhj.ima.facilitron.model.Employee
import at.fhj.ima.facilitron.model.Ticket
import at.fhj.ima.facilitron.model.TicketStatus
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : CrudRepository<Ticket, Int> {

    fun findTicketByTicketStatus(status: TicketStatus):List<Ticket>

    fun findTicketsBySubjectContainingIgnoreCaseOrCategory(subject: String, category: Category):List<Ticket>

    //fun findTicketsByOpenedBy

    fun findTicketById(id: Int):Ticket?

    fun getTicketById(id: Int):Ticket

}