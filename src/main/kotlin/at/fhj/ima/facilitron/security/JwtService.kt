package at.fhj.ima.facilitron.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
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

        return try {val returnMap:MutableMap<String, String> = mutableMapOf()
            DefaultClaim.claimSet.forEach { returnMap[it] = claims[it] as String }
            returnMap.forEach { (t, u) -> println("$t, $u") }

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
        extraClaims : Map<String, Any> = mapOf()
    ):String{
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000*60*5)) // 5 minutes legitimacy
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token:String, userDetails: UserDetails):Boolean{
        val tokenUsermail = extractUsermail(token)
        return (tokenUsermail == userDetails.username && !isTokenExpired(token))
    }

    // checks if token is expired
    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date(System.currentTimeMillis()))
    }


}