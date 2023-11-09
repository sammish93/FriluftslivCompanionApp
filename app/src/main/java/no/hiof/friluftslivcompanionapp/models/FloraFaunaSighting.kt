package no.hiof.friluftslivcompanionapp.models

import java.util.Date

data class FloraFaunaSighting(val species: FloraFauna, val date: Date, val location: Location){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "species" to this.species.toMap(),
            "date" to this.date,
            "location" to this.location.toMap()
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): FloraFaunaSighting? {
            val speciesMap = map["species"] as? Map<String, Any?> ?: emptyMap()
            val species = Bird.fromMap(speciesMap)

            val timestamp = map["date"] as? com.google.firebase.Timestamp
            val date = timestamp?.toDate() ?: return null

            val locationMap = map["location"] as? Map<String, Any?> ?: emptyMap()
            val location = Location.fromMap(locationMap)

            return FloraFaunaSighting(species, date, location)
        }
    }
}
