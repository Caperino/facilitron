package at.fhj.ima.facilitron.model

/**
 * contains all possible authentication levels of the application
 * @author TK Inc.
 */
@Deprecated(message = "will be deleted with next security update", replaceWith = ReplaceWith("SecurityRole"), level = DeprecationLevel.ERROR)
enum class Role {
    EMPLOYEE,
    SUPPORT,
    SECTIONMANAGER,
    HR,
    ADMIN
}
