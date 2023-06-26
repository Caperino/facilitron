package at.fhj.ima.facilitron.security

object DefaultURL {
    // general pages
    const val PUBLIC_LANDING_URL = "/"                  // TODO set correct URL
    const val PUBLIC_TEMP_TESTING = "/public"
    const val CONTACT_URL = "/contact"

    // for authentication redirects
    const val LOGIN_PAGE_URL = "/auth/login"                // TODO set correct URL POST TO AUTH
    const val POST_LOGIN_URL = "/hidden"                    // TODO set correct URL
    const val LOGOUT_PAGE_URL = "/auth/logout"              // TODO set correct URL

    // for register redirects
    const val REGISTER_PAGE_URL = "/auth/register"      // TODO set correct URL

    // URL Filter Exceptions
    const val AUTHENTICATION_PREFIX = "/auth/"
    const val STATIC_PREFIX = "/public"

    // standard URLs
    const val TICKET_URL = "/ticket_overview"
    const val TICKET_DETAILS_URL = "/ticket_details"
    const val TICKET_CLOSE_URL = "/ticket_close"
    const val TICKET_COMMENT_URL = "/ticket_comment"
    const val TICKET_CREATE_URL = "/ticket_create"
    const val USER_URL = "/user_overview"
    const val USER_DETAILS = "/user_details"
    const val USER_CREATE ="/user_create"
    const val USER_EDIT = "/user_edit"
    const val USER_DISABLE_URL = "/user_delete"
    const val NAVPAGE_URL = "/navigation"

    // API URLs
    const val API_SAVE_TIME = "/api/v1/saveTime"
    const val FILE_UPLOAD_URL = "/pfp"

}
