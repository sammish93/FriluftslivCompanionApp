package no.hiof.friluftslivcompanionapp.models.api

data class WeatherResponse(
    val daily: List<DailyWeather>
) {
    data class DailyWeather(
        val weather: List<WeatherDetail>
    )

    data class WeatherDetail(
        val icon: String
    )
}

