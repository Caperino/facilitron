package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO

@Controller
class EmployeeController (
    var employeeService: EmployeeService,
    var fileService: FileService,
    var departmentService: DepartmentService,
    var roleService: RoleService,
    var ocupationService: OcupationService
) {

    @GetMapping(DefaultURL.USER_DETAILS)
    fun userDetails(
        req: HttpServletRequest,
        model: Model,
        @RequestParam(required = false) id:Int
    ):String {
        return if (req.getParameter("id").isNotEmpty()){
            val emp = employeeService.getEmployeeById(id)
            model.addAttribute("employee",emp)
            model.addAttribute("working_history",ocupationService.getOcupationOfEmployee(emp.id!!))
            if (emp.profilePic != null) {
                model.addAttribute("profilePic", emp.profilePic)
            }
            "employeedetails"
        } else {
            val emp = employeeService.getEmployeeById(model.getAttribute("id").toString().toInt())
            model.addAttribute("working_history",ocupationService.getOcupationOfEmployee(emp.id!!))
            model.addAttribute("working_history")
            if (emp.profilePic != null) {
                model.addAttribute("profilePic", emp.profilePic)
            }
            "employeedetails"
        }
    }

    @GetMapping(DefaultURL.USER_URL)
    fun getPageWithAllUser(
        req: HttpServletRequest,
        model: Model,
        @RequestParam(required = false) q:String = ""
    ): String {
        return if (req.getParameter("q").isNotEmpty()){
            val emp = employeeService.findEmployessByName(q)
            model.addAttribute("employees",emp)
            "employeeoverview"
        } else {
            model.addAttribute("employees",employeeService.getAllEmployees())
            "employeeoverview"
        }
    }

    @GetMapping(DefaultURL.USER_CREATE)
    fun userCreate(
        model: Model
    ):String {
        model.addAttribute("departments", departmentService.getAllDepartments())
        model.addAttribute("roles",roleService.getAllRoles())
        return "employeedetails"
    }

}