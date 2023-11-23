package no.hiof.friluftslivcompanionapp.models

import no.hiof.friluftslivcompanionapp.models.enums.WeatherType
import java.time.LocalDate

// This class shows only a single 'weather type' for a whole day. It might be useful to have
// another class that is more detailed and includes weather on an hourly basis.
data class Weather(
    val location: Location,
    val date: LocalDate,
    val windSpeed: Double,
    val temperature: Double,
    val weatherType: WeatherType
)
