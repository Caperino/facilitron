package at.fhj.ima.facilitron.model

import java.security.InvalidParameterException

object TypeSafety {

    //val blockedValues : List<String> = listOf("\n", "\"", "\'", "`", "´", "_", "-", "*", " ", "|")

    private val regex : Regex = Regex("^(?=.*[\"`´\'<>=()|{}])")

    fun checkString(value:String?): Boolean {
        return !value.isNullOrEmpty() && value.isNotBlank()
    }

    fun checkMandatoryParameters(vararg value:String?){
        value.forEach {
            if (it.isNullOrEmpty() || it.isBlank() || it.contains(regex)) throw Exception("'${it}' is invalid as an input value.")
            //blockedValues.forEach { bv -> if (value.contains(bv)) throw Exception("'${it}' is invalid as an input value.") }
        }
    }

}