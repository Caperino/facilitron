package at.fhj.ima.facilitron.controller

import at.fhj.ima.facilitron.model.*
import at.fhj.ima.facilitron.security.DefaultURL
import at.fhj.ima.facilitron.service.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
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
        return "editemployee"
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
        try {
            TypeSafety.checkMandatoryParameters(firstName, secondName, birthday, gender, department, entryDate)
        } catch(e:Exception){
            // TODO error handling
            return errorOccurred("type checking failed, ${e.cause}")
        }

        val em:Employee
        val oldEm:Employee
        val secRoles = roleService.getAllRoles()
        val internalDepartments = departmentService.getAllDepartments()

        if (id != null){
            // override old employee
            oldEm = employeeService.getEmployeeById(id)

            if (password != null && password != "_UNCHANGED_"){
                // incl. password
                try {
                    var file: File? = null
                    if (profilePicture != null && !profilePicture.isEmpty) {
                        file = fileService.createFile(profilePicture)
                    }

                    em = Employee(
                        id = id,
                        firstName = firstName!!,
                        secondName = secondName!!,
                        mail = mail!!,
                        gender = Gender.valueOf(gender ?: ""),
                        password = passwordEncoder.encode(password),
                        birthday = LocalDate.parse(birthday),
                        roles = secRoles.filter { roles?.contains(it.name) ?: false }.toMutableSet(),
                        department = internalDepartments.filter { it.name == department }[0],
                        entryDate = oldEm.entryDate,
                        workingType = WorkingType.valueOf(workingType ?: ""),
                        profilePic = file ?: oldEm.profilePic
                    )

                    employeeService.saveEmployee(em)

                } catch(e:Exception){
                    // TODO return error message and same page
                    return errorOccurred("altering 1.jpg failed, ${e.cause}")
                }

            } else {
                // w/o password
                try {
                    var file: File? = null
                    if (profilePicture != null && !profilePicture.isEmpty) {
                        file = fileService.createFile(profilePicture)
                    }

                    em = Employee(
                        id = id,
                        firstName = firstName!!,
                        secondName = secondName!!,
                        mail = mail!!,
                        gender = Gender.valueOf(gender ?: ""),
                        password = oldEm.password,
                        birthday = LocalDate.parse(birthday),
                        roles = secRoles.filter { roles?.contains(it.name) ?: false }.toMutableSet(),
                        department = internalDepartments.filter { it.name == department }[0],
                        entryDate = oldEm.entryDate,
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

                val ed = try {
                    LocalDate.parse(entryDate)
                } catch (e:Exception){
                    LocalDate.now()
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
                    entryDate = ed,
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
}