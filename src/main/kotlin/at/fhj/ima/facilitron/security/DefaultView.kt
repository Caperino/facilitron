package at.fhj.ima.facilitron.security

/**
 * maps views to corresponding tasks
 * @author TK Inc.
 * @property REGISTER_VIEW where new employee gets created
 * @property POST_REGISTER_VIEW page to load after creation of new employee
 * @property REDIRECTOR NO TOUCHING
 */
object DefaultView{

    // for authentication process
    const val LOGIN_VIEW = "index"                  // TODO set correct page / view

    // for registering process
    const val REGISTER_VIEW = "register"            // TODO set correct page / view
    const val POST_REGISTER_VIEW = "post-register"  // TODO set correct page / view

    // passive redirector (should never be visible)
    const val REDIRECTOR = "redirector"
}
