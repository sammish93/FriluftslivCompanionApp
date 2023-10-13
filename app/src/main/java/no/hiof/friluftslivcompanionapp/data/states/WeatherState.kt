package no.hiof.friluftslivcompanionapp.data.states

import no.hiof.friluftslivcompanionapp.models.Weather

data class WeatherState(
    val currentWeather: Weather? = null,
    val todayWeather: Weather? = null,
    val todayPlusOneWeather: Weather? = null,
    val todayPlusTwoWeather: Weather? = null,
    val todayPlusThreeWeather: Weather? = null,
    val todayPlusFourWeather: Weather? = null,
    val todayPlusFiveWeather: Weather? = null,
    val todayPlusSixWeather: Weather? = null,
    val todayPlusSevenWeather: Weather? = null,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false
)
