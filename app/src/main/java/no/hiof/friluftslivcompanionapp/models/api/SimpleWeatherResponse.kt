package no.hiof.friluftslivcompanionapp.models.api

data class SimpleWeatherResponse(
    val lat: Double,
    val lon: Double,
    val dt: String,
    val windSpeed: Double,
    val temp: Double,
    val icon: String
)
