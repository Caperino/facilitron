package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.TicketComment
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.EmployeeService
import at.fhj.ima.facilitron.service.TicketCommentService
import at.fhj.ima.facilitron.service.TicketService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TicketCommentController(
    val employeeService: EmployeeService,
    val ticketService: TicketService,
    val ticketCommentService: TicketCommentService
) {

    @PostMapping(DefaultURL.TICKET_COMMENT_URL)
    fun addTicketComment(
        model: Model,
        @RequestParam comment: String,
        @RequestParam ticketId: Int
        ): String {
        val employeeId = model.getAttribute("id").toString().toInt()
        val employee = employeeService.getEmployeeById(employeeId)
        val ticket = ticketService.getTicketDetails(ticketId)
        val ticketComment = TicketComment(comment = comment, commenter = employee, ticket = ticket)
        model.addAttribute("ticketComments",ticketCommentService.findTicketCommentsByTicket(ticket))
        return "ticketdetails"
    }
}