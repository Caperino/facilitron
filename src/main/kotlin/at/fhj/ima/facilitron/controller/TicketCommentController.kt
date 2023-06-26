package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.TicketComment
import at.fhj.ima.facilitron.security.DefaultClaim
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.EmployeeService
import at.fhj.ima.facilitron.service.TicketCommentService
import at.fhj.ima.facilitron.service.TicketService
import jakarta.servlet.http.HttpServletRequest
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
        @RequestParam comment: String? = null,
        @RequestParam ticketId: Int,
        req : HttpServletRequest
        ): String {
        DefaultClaim.claimSet.forEach { model.addAttribute(it, req.getAttribute(it))  }

        if (comment.isNullOrEmpty()) {
            errorOccurred("Comment couldn't be empty!",ticketId)
        }

        if (comment!!.length > 125) {
            errorOccurred("Comment maximal length is 125!",ticketId)
        }

        val employeeId = req.getAttribute("id").toString().toInt()
        val employee = employeeService.getEmployeeById(employeeId)

        val ticket = ticketService.getTicketDetails(ticketId)

        // check authority
        val isAllowedPerRoles:Boolean =
            employee.roles.map { it.name }.contains("SUPPORT") || employee.roles.map { it.name }.contains("ADMIN")

        if (employeeId != ticket.openedBy.id && !isAllowedPerRoles){
            return "redirect:/ticket_details?id=${ticketId}"
        }

        val ticketComment = TicketComment(comment = comment!!, commenter = employee, ticket = ticket)
        ticketCommentService.addTicketComment(ticketComment)
        return "redirect:/ticket_details?id=${ticketId}"
    }

    fun errorOccurred(location:String, id:Int):String{
        println("location --> $location")
        return "redirect:/ticket_details?id=${id}"
    }
}