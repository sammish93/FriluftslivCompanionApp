package no.hiof.friluftslivcompanionapp.models

import java.util.Date

data class FloraFaunaSighting(val species: FloraFauna, val date: Date, val location: Location){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "species" to this.species,
            "date" to this.date,
            "location" to this.location.toMap()
        )
    }
}
