package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.Employee
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate
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

    /*@PostMapping(DefaultURL.USER_EDIT)
    fun userEditSave(
        model: Model,
        @RequestParam id:Int? = null,
        @RequestParam firstname:String? = null,
        @RequestParam lastname:String? = null,
        @RequestParam mail:String? = null,
        @RequestParam birthday:String? = null,
        @RequestParam gender:String? = null,
        @RequestParam roles:List<String>? = null,
        @RequestParam workingtype:String? = null,
        @RequestParam entryDate:String? = null,
        @RequestParam department:String? = null,
        @RequestParam password:String? = null,
        @RequestParam profilepicture: MultipartFile? = null
    ): String{
        if (id != null)
            if (firstname != null && lastname != null && birthday != null && gender != null && roles != null && workingtype != null && entryDate != null && department != null && password != null) {
            if (password == "_edit_") {
                val employee = employeeService.getEmployeeById(id);
                val pic = fileService.createFile(profilepicture!!)
                val emp = Employee(id = id, firstName = firstname, secondName = lastname, mail = mail!!, birthday = StringToDate().convert(birthday)!!, gender = StringToGender().convert(gender)!!, password = employee.password,
                    workingType = StringToWorkingType().convert(workingtype)!!, department = departmentService.getDepartmentByName(department), profilePic = pic, roles = roleService.getRolesByName(roles))
            } else {
                val pic = fileService.createFile(profilepicture!!)
                val emp = Employee(id = id, firstName = firstname, secondName = lastname, mail = mail!!, birthday = StringToDate().convert(birthday)!!, gender = StringToGender().convert(gender)!!, password = BCryptPasswordEncoder().encode(password),
                    workingType = StringToWorkingType().convert(workingtype)!!, department = departmentService.getDepartmentByName(department), profilePic = pic, roles = roleService.getRolesByName(roles))
            }
        }

        /*if (bindingResult.hasErrors()){
            println("binding errors")
            return "redirect:/user_overview"
        }
        if (employee != null) {
            employeeService.saveEmployee(employee)
        }*/
        return "redirect:employeedetails?id=${employee?.id}"
    }*/
}