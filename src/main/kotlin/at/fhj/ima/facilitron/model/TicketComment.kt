package at.fhj.ima.facilitron.model

import jakarta.persistence.*

@Entity
class TicketComment (
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    val comment:String,
    @ManyToOne
    val commenter:Employee
)