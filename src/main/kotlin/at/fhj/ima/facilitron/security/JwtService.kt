package at.fhj.ima.facilitron.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jdk.jfr.Timespan
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService {

    // JWT secret key for signing JWT
    private val SECRET_KEY : String = "5970337336763979244226452948404D635166546A576D5A7134743777217A25"

    // Key parsing for internal usage
    private fun getSigningKey(): Key {
        val keyBytes : ByteArray = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    // ------------------------------------------------------------------------------------------

    /**
     * gets us mail saved inside JWT
     */
    fun extractUsermail(token:String):String{
        return extractClaim(token, Claims::getSubject)
    }

    /**
     * gets extra employee information for personalised display
     * @author TK Inc.
     * @param token the JWT to examine
     */
    fun extractPersonalDetails(token:String) : Map<String, String>{
        val claims =  extractAllClaims(token = token)

        return try {
            val returnMap:MutableMap<String, String> = mutableMapOf()
            DefaultClaim.claimSet.forEach { returnMap[it] = claims[it] as String }
            returnMap.forEach { (t, u) -> println("$t --> $u") }

            returnMap
        } catch(e:Exception){
            println("claim extraction failed - ${e.cause}")
            mapOf()
        }
    }

    // gets expiration date of token
    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    /**
     * JWT ID used for possible extension
     * @param token JWT used
     * @author TK Inc.
     * @return current token iteration [0 - 1]
     */
    private fun extractId(token:String):String{
        return extractClaim(token, Claims::getId)
    }

    // gets all information saved inside JWT
    fun extractAllClaims(token:String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims)->T): T {
        val claims = extractAllClaims(token)
        return claimsResolver.invoke(claims)
    }

    // ------------------------------------------------------------------------------------------

    fun generateToken (
        subject : String,
        extraClaims : Map<String, Any> = mapOf(),
        extensionId : String = "0"
    ):String{
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(subject)
            .setId(extensionId)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000*60*60*4)) // 4 hours legitimacy
            //.setExpiration(Date(System.currentTimeMillis() + 1000*60*16)) // 16 minutes legitimacy
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    @Deprecated("userDetails will not be support further", level = DeprecationLevel.WARNING)
    fun isTokenValid(token:String, userDetails: UserDetails):Boolean{
        val tokenUsermail = extractUsermail(token)
        return (tokenUsermail == userDetails.username && !isTokenExpired(token))
    }

    fun isTokenValid(token:String):Boolean{
        return !isTokenExpired(token = token)
    }

    /**
     * use to identify whether tokens are subject to extension
     * @authorTK Inc.
     * @param token currently given token
     */
    fun allowTokenExtension(token:String):Boolean{
        return extractId(token) == "0"
    }

    /**
     * returns a new token with 4 hours validity
     * @param token the soon expiring token
     * @author TK Inc.
     * @return a new valid token
     */
    fun extendToken(token:String):String{
        return generateToken(extractUsermail(token), extractPersonalDetails(token), "1")
    }

    /**
     * check token validity
     * @param token user JWT token
     * @throws Exception instead of false
     * @author TK Inc.
     */
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).toInstant().minusMillis(1000*60*15).isBefore(Date(System.currentTimeMillis()).toInstant())
    }

}