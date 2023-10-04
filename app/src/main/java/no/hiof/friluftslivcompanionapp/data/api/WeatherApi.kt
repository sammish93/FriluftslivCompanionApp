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

    // eBird API Service instance.
    private val weatherApiService: WeatherApiService by lazy {
        val retrofit = RetrofitBuilder.buildWeatherApi()
        retrofit.create(WeatherApiService::class.java)
    }

    suspend fun getWeatherInfo(
        lat: Double,
        lon: Double,
        apiKey: String = BuildConfig.WEATHER_API_KEY
    ): Result<SimpleWeatherResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApiService.getWeatherToday(lat, lon, apiKey)
                handleResponse(response)
            } catch (e: Exception) {
                Result.Failure(e.message ?: "Unknown failure")
            }
        }
    }

    // This method handles the response from the Wikipedia API.
    // It checks if the response is successful and returns a Result object accordingly.
    private fun handleResponse(response: Response<WeatherResponse>): Result<SimpleWeatherResponse?> {
        return if (response.isSuccessful) {
            val weatherResponse = response.body()
            weatherResponse?.let { Result.Success(mapToSimpleResponse(it)) }
                ?: Result.Failure("No body in response")
        } else {
            Result.Failure("Unsuccessful response: ${response.errorBody()?.string()}")
        }
    }

    // This method maps a WikipediaResponse object to a SimpleWikipediaResponse object.
    private fun mapToSimpleResponse(weatherResponse: WeatherResponse): SimpleWeatherResponse? {

        val icons = weatherResponse.daily.map { it.weather.first().icon }
        val firstIcon = icons.first()

        return SimpleWeatherResponse(
            icon = firstIcon
        )
    }

    /*
    private fun mapToWeather(weatherToday: SimpleWeatherResponse): Weather {
        var location = Location(62.6259, 7.0867)
        return Weather(location, LocalDate.now(), WeatherType.CLEAR_SKY)
    }
     */
}