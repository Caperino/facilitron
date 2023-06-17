package at.fhj.ima.facilitron.model

import jakarta.persistence.*
import java.sql.Time

/**
 * Base Class for all ticket-comments
 * @author SJB
 * @param id is AutoGenerated.
 * @param comment is the actual comment text
 * @param commenter is the employee who wrote the comment
 * @param postedTime is the time the comment was posted
 */
@Entity
class TicketComment (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val comment:String,
    @ManyToOne
    val commenter:Employee,
    val postedTime:Time = Time(System.currentTimeMillis())
)