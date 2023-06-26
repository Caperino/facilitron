package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.security.DefaultClaim
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.cglib.core.Local
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.imageio.ImageIO

@Controller
class EmployeeController (
    var employeeService: EmployeeService,
    var fileService: FileService,
    var departmentService: DepartmentService,
    var roleService: RoleService,
    var ocupationService: OcupationService,
    val passwordEncoder: PasswordEncoder
) {

    @GetMapping(DefaultURL.USER_DETAILS)
    fun userDetails(
        req: HttpServletRequest,
        model: Model,
        @RequestParam(required = false) id:Int?
    ):String {
        DefaultClaim.claimSet.forEach { model.addAttribute(it, req.getAttribute(it))  }
        return if (id != null){
            try {
                val emp = employeeService.getEmployeeById(id)
                model.addAttribute("employee",emp)
                model.addAttribute("ocuptions",ocupationService.getOcuptationOfEmployee(emp))
                return "employeedetails"
            } catch (e:Exception) {
                model.addAttribute("error", "Employee not found.")
                return "forward:/user_overview"
            }
        } else {
            val emp = employeeService.getEmployeeById(req.getAttribute("id").toString().toInt())
            model.addAttribute("employee",emp)
            model.addAttribute("ocuptions",ocupationService.getOcuptationOfEmployee(emp))

            "employeedetails"
        }
    }

    @GetMapping(DefaultURL.USER_URL)
    fun getPageWithAllUser(
        req: HttpServletRequest,
        model: Model,
        @RequestParam(required = false) q:String? = "",
        @RequestParam(required = false) ac:String?
    ): String {
        DefaultClaim.claimSet.forEach { model.addAttribute(it, req.getAttribute(it))  }
        if (ac != null){
            if (ac=="err"){
                model.addAttribute("error", "We encountered an error while deleting this user.")
            } else {
                model.addAttribute("succ", "Employee has been deactivated.")
            }
        }
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
        model: Model,
        req:HttpServletRequest
    ):String {
        DefaultClaim.claimSet.forEach { model.addAttribute(it, req.getAttribute(it))  }
        model.addAttribute("departments", departmentService.getAllDepartments())

        val secRoles = roleService.getAllRoles()
        val safeSecRoles = secRoles.toMutableList()
        safeSecRoles.removeIf { it.name == "ADMIN" }
        model.addAttribute("secRoles", secRoles)
        model.addAttribute("safeSecRoles", safeSecRoles)

        model.addAttribute("isCreate", true)
        return "editemployee"
    }

    @GetMapping(DefaultURL.USER_EDIT)
    fun userEdit(
        model: Model,
        req: HttpServletRequest,
        @RequestParam id:Int
    ):String {
        DefaultClaim.claimSet.forEach { model.addAttribute(it, req.getAttribute(it))  }
        model.addAttribute("employee",employeeService.getEmployeeById(id))
        model.addAttribute("departments", departmentService.getAllDepartments())

        val secRoles = roleService.getAllRoles()
        val safeSecRoles = secRoles.toMutableList()
        safeSecRoles.removeIf { it.name == "ADMIN" }
        model.addAttribute("secRoles", secRoles)
        model.addAttribute("safeSecRoles", safeSecRoles)

        model.addAttribute("isCreate", false)
        return "editemployee"
    }

    @GetMapping(DefaultURL.FILE_UPLOAD_URL + "/{id}")
    fun getProfilePicture(@PathVariable("id") id:Int):ResponseEntity<Any>{
        val fileOpt = fileService.findById(id)
        val path = fileService.retrievePath(id)
        val file:File = if (fileOpt.isPresent) fileOpt.get() else return ResponseEntity.notFound().build()

        val fileSystemResource = FileSystemResource(path)
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=" + file.originalFileName)
            .contentType(MediaType.parseMediaType(file.contentType!!))
            .body(fileSystemResource)
    }

    @PostMapping(DefaultURL.USER_EDIT)
    fun userEditSave(
        model: Model,
        req:HttpServletRequest,
        @RequestParam id:Int? = null,
        @RequestParam mail:String? = null,
        @RequestParam firstName:String? = null,
        @RequestParam secondName:String? = null,
        @RequestParam birthday:String? = null,
        @RequestParam gender:String? = null,
        @RequestParam profilePicture: MultipartFile? = null,
        @RequestParam roles:List<String>? = null,
        @RequestParam workingType:String? = null,
        @RequestParam department:String? = null,
        @RequestParam entryDate:String? = null,
        @RequestParam password:String? = null,
    ): String{
        val entryDateAsDate:LocalDate

        try {
            TypeSafety.checkMandatoryParameters(firstName, secondName, birthday, gender, department, entryDate)
            entryDateAsDate = LocalDate.parse(entryDate)
        } catch(e:Exception){
            // TODO error handling
            return errorOccurred("type checking failed, ${e.message}")
        }

        if (firstName!!.length > 40 || secondName!!.length > 40 || (password?.length ?: 0) > 40) {
            return errorOccurred("length checking failed, firstname, lastname or password is longer than 40 Characters")
        }

        val em:Employee
        val oldEm:Employee
        val secRoles = roleService.getAllRoles()

        val roleString : String = req.getAttribute("roles") as String
        if (!roleString.contains("ADMIN")) (secRoles as MutableList).removeIf {it.name == "ADMIN"}

        val internalDepartments = departmentService.getAllDepartments()

        if (id != null){
            // override old employee
            oldEm = employeeService.getEmployeeById(id)

            if (password != null && password != "_UNCHANGED_" && roleString.contains("ADMIN")){
                // incl. password | ADMIN
                try {
                    var file: File? = null
                    if (profilePicture != null && !profilePicture.isEmpty) {
                        file = fileService.createFile(profilePicture)
                    }

                    em = Employee(
                        id = id,
                        firstName = firstName!!,
                        secondName = secondName!!,
                        mail = oldEm.mail,
                        gender = Gender.valueOf(gender ?: ""),
                        password = passwordEncoder.encode(password),
                        birthday = LocalDate.parse(birthday),
                        roles = secRoles.filter { roles?.contains(it.name) ?: false }.toMutableSet(),
                        department = internalDepartments.filter { it.name == department }[0],
                        entryDate = entryDateAsDate,
                        workingType = WorkingType.valueOf(workingType ?: ""),
                        profilePic = file ?: oldEm.profilePic
                    )

                    employeeService.saveEmployee(em)

                } catch(e:Exception){
                    // TODO return error message and same page
                    return errorOccurred("altering 1.jpg failed, ${e.cause}")
                }

            } else {
                // w/o password | HR
                try {
                    var file: File? = null
                    if (profilePicture != null && !profilePicture.isEmpty) {
                        file = fileService.createFile(profilePicture)
                    }



                    em = Employee(
                        id = id,
                        firstName = firstName!!,
                        secondName = secondName!!,
                        mail = oldEm.mail,
                        gender = Gender.valueOf(gender ?: ""),
                        password = oldEm.password,
                        birthday = LocalDate.parse(birthday),
                        roles = secRoles.filter { roles?.contains(it.name) ?: false }.toMutableSet(),
                        department = internalDepartments.filter { it.name == department }[0],
                        entryDate = entryDateAsDate,
                        workingType = WorkingType.valueOf(workingType ?: ""),
                        profilePic = file ?: oldEm.profilePic
                    )

                    employeeService.saveEmployee(em)

                } catch(e:Exception){
                    // TODO return error message and same page
                    return errorOccurred("altering 2 failed, ${e.cause}")
                }
            }

        } else {
            // create new employee
            try {
                var file: File? = null
                if (profilePicture != null && !profilePicture.isEmpty) {
                    file = fileService.createFile(profilePicture)
                }


                em = Employee(
                    firstName = firstName!!,
                    secondName = secondName!!,
                    mail = "${firstName}.${secondName}@facilitron.org",
                    gender = Gender.valueOf(gender ?: ""),
                    password = passwordEncoder.encode(password),
                    birthday = LocalDate.parse(birthday),
                    roles = secRoles.filter { roles?.contains(it.name) ?: false }.toMutableSet(),
                    department = internalDepartments.filter { it.name == department }[0],
                    entryDate = entryDateAsDate,
                    workingType = WorkingType.valueOf(workingType ?: ""),
                    profilePic = file
                )

                employeeService.saveEmployee(em)

            } catch(e:Exception){
                // TODO error handling
                return errorOccurred("creation failed, ${e.message}")
            }
        }

        return "redirect:/user_overview"
    }

    fun errorOccurred(location:String):String{
        println("location --> $location")
        return "redirect:/user_overview"
    }

    @PostMapping(DefaultURL.USER_DISABLE_URL)
    fun setEmployeeDisabled(
        model: Model,
        @RequestParam id: Int? = null,
        req: HttpServletRequest
    ): String {
        if (id != null){
            val employee = employeeService.getEmployeeById(id)
            if (employeeService.deleteEmployee(employee)){
                return "redirect:/user_overview?ac=succ"
            } else {
                return "redirect:/user_overview?ac=err"
            }
        }
        return "redirect:/user_overview?ac=err"
    }
}