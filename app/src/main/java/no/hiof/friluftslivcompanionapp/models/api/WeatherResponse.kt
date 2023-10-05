package no.hiof.friluftslivcompanionapp.models.api

data class WeatherResponse(
    val current: DailyWeather,
    val daily: List<DailyWeather>
) {
    data class DailyWeather(
        val dt: String,
        val wind_speed: Double,
        val weather: List<WeatherDetail>
    )

    data class WeatherDetail(
        val icon: String
    )
}

