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
        fun fromMap(map: Map<String, Any?>): FloraFaunaSighting {
            val speciesMap = map["species"] as? Map<String, Any?> ?: emptyMap()
            val species = Bird.fromMap(speciesMap)

            val date = map["date"] as? Date ?: Date()

            val locationMap = map["location"] as? Map<String, Any?> ?: emptyMap()
            val location = Location.fromMap(locationMap)

            return FloraFaunaSighting(species, date, location)
        }
    }
}
