package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.Employee
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
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
        @RequestParam(required = false) id:Int?
    ):String {
        return if (id != null){
            val emp = employeeService.getEmployeeById(id)
            model.addAttribute("employee",emp)
            model.addAttribute("ocuptions",ocupationService.getOcuptationOfEmployee(emp))
            /* if (emp.profilePic != null) {
                model.addAttribute("profilePic", emp.profilePic)
            } */
            "employeedetails"
        } else {
            val emp = employeeService.getEmployeeById(req.getAttribute("id").toString().toInt())
            model.addAttribute("employee",emp)
            model.addAttribute("ocuptions",ocupationService.getOcuptationOfEmployee(emp))
            /* if (emp.profilePic != null) {
                model.addAttribute("profilePic", emp.profilePic)
            } */
            "employeedetails"
        }
    }

    @GetMapping(DefaultURL.USER_URL)
    fun getPageWithAllUser(
        req: HttpServletRequest,
        model: Model,
        @RequestParam(required = false) q:String? = ""
    ): String {
        return if (req.getParameter("q") != null){
            val emp = employeeService.findEmployeesByName(q!!)
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
        model.addAttribute("isCreate", true)
        return "newemployee"
    }

    @GetMapping(DefaultURL.USER_EDIT)
    fun userEdit(
        model: Model,
        @RequestParam id:Int
    ):String {
        model.addAttribute("employee",employeeService.getEmployeeById(id))
        model.addAttribute("departments", departmentService.getAllDepartments())
        model.addAttribute("roles",roleService.getAllRoles())
        model.addAttribute("isCreate", false)
        return "editemployee"
    }

    @PostMapping(DefaultURL.USER_EDIT)
    fun userEditSave(
        /*model: Model,
        @RequestParam firstname:String,
        @RequestParam lastname:String,
        @RequestParam birthday:String,
        @RequestParam gender:String,
        @RequestParam roles:String,
        @RequestParam workingType:String,
        @RequestParam entryDate:String,
        @RequestParam department:String*/
        @ModelAttribute @Valid employee: Employee?,
        bindingResult: BindingResult,
        model: Model,
    ): String{
        if (bindingResult.hasErrors()){
            println("binding errors")
            return "redirect:/user_overview"
        }
        if (employee != null) {
            employeeService.saveEmployee(employee)
        }
        return "redirect:employeedetails?id=${employee?.id}"
    }
}