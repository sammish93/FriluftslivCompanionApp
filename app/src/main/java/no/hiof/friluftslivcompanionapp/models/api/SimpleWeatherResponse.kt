package no.hiof.friluftslivcompanionapp.models.api

data class SimpleWeatherResponse(
    val dt: String,
    val windSpeed: Double,
    val temp: Double,
    val icon: String
)
