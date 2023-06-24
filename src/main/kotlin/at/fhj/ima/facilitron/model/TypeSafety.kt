package at.fhj.ima.facilitron.model

object TypeSafety {

    fun checkString(value:String?): Boolean {
        return !value.isNullOrEmpty() && value.isNotBlank()
    }

    fun checkMandatoryParameters(vararg value:String?){
        value.forEach { if (it.isNullOrEmpty() || it.isBlank()) throw Exception() }
    }

}