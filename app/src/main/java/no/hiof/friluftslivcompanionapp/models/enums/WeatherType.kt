package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class WeatherType(val resourcePath: Int) {
    CLEAR_SKY(R.drawable._1d),
    FEW_CLOUDS(R.drawable._2d),
    SCATTERED_CLOUDS(R.drawable._3d),
    BROKEN_CLOUDS(R.drawable._4d),
    SHOWER_RAIN(R.drawable._9d),
    RAIN(R.drawable._10d),
    THUNDERSTORM(R.drawable._11d),
    SNOW(R.drawable._13d),
    MIST(R.drawable._50d)
}