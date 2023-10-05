package no.hiof.friluftslivcompanionapp.models.api

data class WeatherResponse(
    val current: CurrentWeather,
    val daily: List<DailyWeather>
) {
    data class CurrentWeather(
        val dt: String,
        val wind_speed: Double,
        val temp: Double,
        val weather: List<WeatherDetail>
    )
    data class DailyWeather(
        val dt: String,
        val wind_speed: Double,
        val temp: TempDetail,
        val weather: List<WeatherDetail>
    )

    data class TempDetail(
        val day: Double
    )

    data class WeatherDetail(
        val icon: String
    )
}

