package at.fhj.ima.facilitron.model

class RegisterRequest(
    val firstname:String,
    val secondname:String,
    val mail:String,
    val password:String,
    val phone:String? = null
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
    val phone:String? = null
) : UnsafeData {

    override fun evaluateState():Boolean{
        return (firstname != null && secondname != null && mail != null && password != null)
    }

}