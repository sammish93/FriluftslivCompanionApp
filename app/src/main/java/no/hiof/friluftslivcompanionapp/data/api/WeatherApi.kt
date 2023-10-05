package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.BuildConfig
import no.hiof.friluftslivcompanionapp.data.api.http_interface.WeatherApiService
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.api.SimpleWeatherResponse
import no.hiof.friluftslivcompanionapp.models.api.WeatherResponse
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherApi {

    // OpenWeatherMaps API Service instance.
    private val weatherApiService: WeatherApiService by lazy {
        val retrofit = RetrofitBuilder.buildWeatherApi()
        retrofit.create(WeatherApiService::class.java)
    }

    // Function to get weather information based on a location (longitude and latitude). This
    // function returns a list of 9 SimpleWeatherResponse objects.
    // [0] is the current weather.
    // [1] is a summary of today's weather.
    // [2]-[8] are a summary of the weather in the following 7 days.
    suspend fun getWeatherInfo(
        lat: Double,
        lon: Double,
        exclude: String = "minutely,hourly",
        units: String = "metric",
        apiKey: String = BuildConfig.WEATHER_API_KEY
    ): Result<List<SimpleWeatherResponse>?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApiService.getWeatherToday(lat, lon, exclude, units, apiKey)
                handleResponse(response)
            } catch (e: Exception) {
                Result.Failure(e.message ?: "Unknown failure")
            }
        }
    }

    // This method handles the response from the Wikipedia API.
    // It checks if the response is successful and returns a Result object accordingly.
    private fun handleResponse(response: Response<WeatherResponse>): Result<List<SimpleWeatherResponse>?> {
        return if (response.isSuccessful) {
            val weatherResponse = response.body()
            weatherResponse?.let { Result.Success(mapToSimpleResponse(it)) }
                ?: Result.Failure("No body in response")
        } else {
            Result.Failure("Unsuccessful response: ${response.errorBody()?.string()}")
        }
    }

    // This method maps a WikipediaResponse object to a SimpleWikipediaResponse object.
    private fun mapToSimpleResponse(weatherResponse: WeatherResponse): List<SimpleWeatherResponse>? {

        val listToReturn = mutableListOf<SimpleWeatherResponse>()

        val currentWeather = weatherResponse.current
        val currentDate = currentWeather.dt
        val currentWindSpeed = currentWeather.wind_speed
        val currentTemp = currentWeather.temp
        val currentIcon = currentWeather.weather.first().icon

        listToReturn.add(
            SimpleWeatherResponse(
                dt = currentDate,
                windSpeed = currentWindSpeed,
                temp = currentTemp,
                icon = currentIcon
            )
        )

        val forecast = weatherResponse.daily.map {
            val date = it.dt
            val windSpeed = it.wind_speed
            val temp = it.temp.day
            val icon = it.weather.first().icon
            listToReturn.add(
                SimpleWeatherResponse(
                    dt = date,
                    windSpeed = windSpeed,
                    temp = temp,
                    icon = icon
                )
            )
        }

        return listToReturn
    }
}