package at.fhj.ima.facilitron.model

import java.time.LocalDate

class RegisterRequest(
    val firstname:String,
    val secondname:String,
    val mail:String,
    val password:String,
    val phone:String? = null,
    val gender:Gender,
    val birthday: LocalDate
) {
}

/**
 * class specifically for incoming requests with unknown content
 * @author Tk Inc.
 */
class UnsafeRegisterRequest(
    val firstname:String?,
    val secondname:String?,
    val mail:String?,
    val password:String?,
    val phone:String? = null,
    val gender:Gender?,
    val birthday:LocalDate?
) : UnsafeData {

    override fun evaluateState():Boolean{
        return (firstname != null && secondname != null && mail != null && password != null)
    }

}