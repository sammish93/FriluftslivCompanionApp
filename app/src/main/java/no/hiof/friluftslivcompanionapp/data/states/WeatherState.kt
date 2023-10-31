package no.hiof.friluftslivcompanionapp.data.states

import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.enums.WeatherUnit

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
    val currentWeatherForSearch: Weather? = null,
    val todayWeatherForSearch: Weather? = null,
    val todayPlusOneWeatherForSearch: Weather? = null,
    val todayPlusTwoWeatherForSearch: Weather? = null,
    val todayPlusThreeWeatherForSearch: Weather? = null,
    val todayPlusFourWeatherForSearch: Weather? = null,
    val todayPlusFiveWeatherForSearch: Weather? = null,
    val todayPlusSixWeatherForSearch: Weather? = null,
    val todayPlusSevenWeatherForSearch: Weather? = null,
    val unitChoice: WeatherUnit = WeatherUnit.METRIC,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
    val isNoGps: Boolean = false
)
