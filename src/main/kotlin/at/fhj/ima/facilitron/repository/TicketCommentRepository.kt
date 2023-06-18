package at.fhj.ima.facilitron.repository

import at.fhj.ima.facilitron.model.TicketComment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TicketCommentRepository : CrudRepository<TicketComment, Int> {

    fun findTicketCommentsByCommenterId(commenterId: Int):List<TicketComment>?


}