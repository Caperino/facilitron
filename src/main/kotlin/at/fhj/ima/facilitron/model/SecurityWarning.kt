package at.fhj.ima.facilitron.model

enum class SecurityWarning(
    val message:String
) {
    // Authentication Process
    FAILEDAUTHENTICATION(message = "Wrong mail or password, please try again."),
    FAILEDDATARETRIEVAL(message = "We are experiencing trouble, please try again."),

    // Register Process
    EMPLOYEEALREADYEXISTS(message = "Employee already exists."),
    MISSINGVALUES(message = "Not all needed values are provided.")
}