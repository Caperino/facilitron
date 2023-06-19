package at.fhj.ima.facilitron.security

/**
 * contains all keys for saved information inside JWT to prevent losses
 * @author TK Inc.
 */
object DefaultClaim {

    /**
     * all claim keys for extraction
     */
    val claimSet = setOf("id","firstName", "secondName", "roles", "mail")

}