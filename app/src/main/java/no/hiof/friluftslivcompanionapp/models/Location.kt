package no.hiof.friluftslivcompanionapp.models

// The properties 'lat' and 'lon' (latitude and longitude) are currently mutable (var).
data class Location(var lat: Double, var lon: Double){
    fun toMap(): Map<String, Double> {
        return mapOf(
            "lat" to lat,
            "lon" to lon
        )
    }


}
