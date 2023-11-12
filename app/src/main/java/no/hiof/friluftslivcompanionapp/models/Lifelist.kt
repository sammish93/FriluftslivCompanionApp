package no.hiof.friluftslivcompanionapp.models

data class Lifelist(
    val sightings: FloraFaunaSighting
){
    fun toMap(): Map<String, Any?>{
        return mapOf(
            "sightings" to sightings.toMap()
        )
    }
}
