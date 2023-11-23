package no.hiof.friluftslivcompanionapp.models.enums

enum class WeatherTriggers(val threshold: Int=0, val isActive: Boolean=false) {
    WIND_IN_METER(10),
    THUNDERSTORM(isActive = true),
    SNOWFALL(isActive = true)
}