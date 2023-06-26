package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.*
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TicketRepository : CrudRepository<Ticket, Int> {

    fun findTicketByTicketStatus(status: TicketStatus):List<Ticket>

    fun findTicketsByCategory(category: Category):List<Ticket>

    fun findTicketsBySubjectContainingIgnoreCase(subject: String):List<Ticket>

    fun findTicketsByOpenedByOrClosedBy(employee1: Employee, employee2: Employee):List<Ticket>

    fun findTicketById(id: Int):Ticket?

    fun getTicketById(id: Int):Ticket

}