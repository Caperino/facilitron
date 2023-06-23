package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.Priority
import at.fhj.ima.facilitron.model.Ticket
import at.fhj.ima.facilitron.model.TicketComment
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TicketController (
    val ticketService: TicketService,
    val categoryService: CategoryService,
    val ticketCommentService: TicketCommentService,
    val employeeService: EmployeeService
) {
    @GetMapping(DefaultURL.TICKET_URL)
    fun getPageWithAllTickets(
        req: HttpServletRequest,
        model: Model,
        @RequestParam(required = false) q:String = ""
    ) : String {
        return if (req.getParameter("q").isNotEmpty()){
            val cats = categoryService.getCategoriesByName(q)
            model.addAttribute("tickets",ticketService.searchTickets(cats, q))
            "ticket_overview"
        } else {
            model.addAttribute("tickets",ticketService.getAllTickets())
            "ticket_overview"
        }
    }

    @GetMapping(DefaultURL.TICKET_DETAILS_URL)
    fun getTicketDetails(
        model: Model,
        @RequestParam id:Int
    ) : String {
        val tk : Ticket
        val com : List<TicketComment>
        try {
            tk = ticketService.getTicketDetails(id)
            com = ticketCommentService.findTicketCommentsByTicket(tk)
        } catch (_:Exception) {
            model.addAttribute("error","Ticket not found!")
            return "ticket_overview"
        }
        model.addAttribute("ticketDetails",tk)
        model.addAttribute("ticketComments",com)
        return "ticketdetails"
    }

    @GetMapping(DefaultURL.TICKET_CLOSE_URL)
    fun closeTicket(
        model: Model,
        @RequestParam id: Int
    ): String {
        val employee = employeeService.getEmployeeById(model.getAttribute("id").toString().toInt())
        val tk = ticketService.getTicketDetails(id)
        return if (ticketService.closeTicket(tk, employee)) {
            model.addAttribute("tickets",ticketService.getAllTickets())
            "ticket_overview"
        } else {
            model.addAttribute("error", "Ticket couldn't be deleted!")
            "ticket_overview"
        }
    }

    @GetMapping(DefaultURL.TICKET_CREATE_URL)
    fun createTicket(
        model: Model,
        req : HttpServletRequest,
        @RequestParam(required = false) subject: String,
        @RequestParam(required = false) priority: String,
        @RequestParam(required = false) category: String,
        @RequestParam(required = false) description: String
    ): String {
        if (req.getParameter("subject").isNotEmpty() || req.getParameter("priority").isNotEmpty() || req.getParameter("category").isNotEmpty() || req.getParameter("description").isNotEmpty()) {
            return try {
                val prio = StringToPriority().convert(priority)!!
                val cat = categoryService.getCategoryByName(category)
                val employee = employeeService.getEmployeeById(model.getAttribute("id").toString().toInt())
                val tk = Ticket(priority = prio, category = cat, subject = subject, description = description, openedBy = employee)
                "ticket_overview"
            } catch (e: Exception) {
                model.addAttribute("error", "Couldn't open the Ticket!")
                "newticket"
            }
        } else {
            model.addAttribute("category", categoryService.getAllCategories())
            return "newticket"
        }
    }
}