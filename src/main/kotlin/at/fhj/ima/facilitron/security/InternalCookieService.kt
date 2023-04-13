package at.fhj.ima.facilitron.security

import jakarta.servlet.http.Cookie
import org.springframework.stereotype.Service

@Service
class InternalCookieService{
    /**
     * generates a default cookie used throughout the application
     * @param token the JWT to be stored inside the cookie
     * @author TK Inc.
     * @return valid Cookie containing JWT
     */
    fun generateAuthCookie(token:String): Cookie {
        val newCookie = Cookie("auth", token)

        // generally for whole application
        newCookie.path = "/"

        // prevent JS scraping
        newCookie.isHttpOnly = true

        // in seconds, currently 24h
        newCookie.maxAge = 60*60*24

        // valid domains for the cookie
        // newCookie.domain = "localhost"

        // to use only via secured connections
        // newCookie.secure = true

        return newCookie
    }

    /**
     * overrides current cookie with expired and empty one
     * @return Cookie (expired and invalid)
     * @author TK Inc.
     */
    fun deleteAuthCookie():Cookie{
        val newCookie = Cookie("auth", null)

        newCookie.path = "/"
        newCookie.isHttpOnly = true
        newCookie.maxAge = 0

        return newCookie
    }
}