package no.hiof.friluftslivcompanionapp.models

// The properties 'lat' and 'lon' (latitude and longitude) are currently mutable (var).
data class Location(var lat: Double, var lon: Double){
    fun toMap(): Map<String, Double> {
        return mapOf(
            "lat" to lat,
            "lon" to lon
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): Location {
            val lat = map["lat"] as? Double ?: throw IllegalArgumentException("Latitude is missing")
            val lon = map["lon"] as? Double ?: throw IllegalArgumentException("Longitude is missing")
            return Location(lat, lon)
        }
    }


}
