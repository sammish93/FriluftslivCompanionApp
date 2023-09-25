package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class WeatherType(val resourcePath: Int) {
    CLEAR_SKY(R.drawable.ic_launcher_foreground),
    FEW_CLOUDS(R.drawable.ic_launcher_foreground),
    SCATTERED_CLOUDS(R.drawable.ic_launcher_foreground),
    BROKEN_CLOUDS(R.drawable.ic_launcher_foreground),
    SHOWER_RAIN(R.drawable.ic_launcher_foreground),
    RAIN(R.drawable.ic_launcher_foreground),
    THUNDERSTORM(R.drawable.ic_launcher_foreground),
    SNOW(R.drawable.ic_launcher_foreground),
    MIST(R.drawable.ic_launcher_foreground)
}