package no.hiof.friluftslivcompanionapp.models

// List vs Array choice. Apparently List makes things much easier for Kotlin.
data class WeatherForecast(val location: Location, val forecast: List<Weather>)
