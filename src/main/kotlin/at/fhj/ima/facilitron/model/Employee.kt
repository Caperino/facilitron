package at.fhj.ima.facilitron.model

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate

/**
 * Base Class for all users
 * @author TK Inc. revised by SJB
 * @param id is AutoGenerated
 * @param firstName
 * @param secondName
 * @param mail is used as Username
 * @param gender
 * @param password
 * @param phone number of the employee
 * @param birthday
 * @param accountStatus information about employee status
 * @param roles contains access authority of user
 * @param department is the employees assigned department
 * @param entryDate is the Date the employee joined the company
 * @param workingType is the type of employment
 * @param profilePic is the profile picture of the employee
 */
@Entity
class Employee(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Int? = null,
    var firstName:String,
    var secondName:String,
    var mail:String,
    var gender:Gender,
    private var password:String,
    private var phone:String? = null,
    val birthday:LocalDate,
    private var accountStatus:AccountStatus = AccountStatus.ACTIVE,
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "employee_roles",
        joinColumns = [JoinColumn(name = "employee_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles:MutableSet<SecurityRole> = mutableSetOf(),
    @ManyToOne(fetch = FetchType.EAGER)
    var department: Department? = null,
    private var entryDate: LocalDate = LocalDate.now(),
    private var workingType: WorkingType = WorkingType.FULLTIME,
    @OneToOne
    val profilePic:File? = null
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.name) } as MutableList<out GrantedAuthority>
    }

    fun addAuthority(role:SecurityRole){
        roles.add(role)
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return mail
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    fun getEntryDate(): LocalDate {
        return entryDate
    }

    fun getWorkingType(): WorkingType {
        return workingType
    }

    override fun isAccountNonLocked(): Boolean {
        return accountStatus != AccountStatus.LOCKED
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return accountStatus == AccountStatus.ACTIVE
    }

    /**
     * extracts basic information about employee saved in JWT
     */
    operator fun get(parameter: String): String {
        return when (parameter){
            "id" -> id.toString()
            "firstName" -> firstName
            "secondName" -> secondName
            "mail" -> username
            "roles" -> (roles.fold("") { acc, securityRole -> acc + securityRole.name + ";" }).removeSuffix(";")
            else -> ""
        }
    }

    fun concatRoles():String{
        return roles.fold("") { acc, securityRole -> acc + "${securityRole.name}, " }.removeSuffix(", ")
    }

}