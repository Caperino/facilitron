package at.fhj.ima.facilitron.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtUserDetails(
    val sub : String,
    val firstName : String,
    val secondName : String,
    //val authorities : MutableCollection<out GrantedAuthority>,
    private val roles : List<String>,
    val id : String
) : UserDetails{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it) } as MutableList<out GrantedAuthority>
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return sub
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    operator fun get(parameter: String): String {
        return when (parameter){
            "id" -> id
            "firstName" -> firstName
            "secondName" -> secondName
            "mail" -> sub
            "roles" -> (roles.fold("") { acc, role -> "$acc$role;" }).removeSuffix(";")
            else -> ""
        }
    }
}