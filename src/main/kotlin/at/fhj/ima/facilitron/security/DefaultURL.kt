package at.fhj.ima.facilitron.security

object DefaultURL {
    // general pages
    const val PUBLIC_LANDING_URL = "/"                  // TODO set correct URL
    const val PUBLIC_TEMP_TESTING = "/public"
    const val CONTACT_URL = "/contact"

    // for authentication redirects
    const val LOGIN_PAGE_URL = "/#Login"                // TODO set correct URL POST TO AUTH
    const val POST_LOGIN_URL = "/hidden"                // TODO set correct URL
    const val LOGOUT_PAGE_URL = "/auth/logout"          // TODO set correct URL

    // for register redirects
    const val REGISTER_PAGE_URL = "/auth/register"      // TODO set correct URL
    const val POST_REGISTER_URL = "/public"             // TODO set correct URL

    // URL Filter Exceptions
    const val AUTHENTICATION_PREFIX = "/auth/"
    const val STATIC_PREFIX = "/public"

    // standard URLs
    const val TICKET_URL = "/tickets"
    const val TICKET_DETAILS_URL = "/ticket_details"

}
