package at.fhj.ima.facilitron.model

class AuthenticationResponse(
    val token:String,
    val exception:SecurityWarning? = null
){
}