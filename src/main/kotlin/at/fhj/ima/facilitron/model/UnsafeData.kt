package at.fhj.ima.facilitron.model

/**
 * for classes that might contain data of unknown quality
 */
interface UnsafeData {
    /**
     * returns safety state of all properties
     */
    fun evaluateState():Boolean
}