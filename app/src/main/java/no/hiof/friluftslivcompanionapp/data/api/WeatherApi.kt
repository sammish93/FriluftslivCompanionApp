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
        exclude: String = "minutely,hourly",
        apiKey: String = BuildConfig.WEATHER_API_KEY
    ): Result<List<SimpleWeatherResponse>?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApiService.getWeatherToday(lat, lon, exclude, apiKey)
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

        //val firstDay = weatherResponse.daily.map { it.weather.first().icon }
        //val firstDate = weatherResponse.daily.first().dt
        //val firstIcon = firstDay.first()

        /*
        val forecast = weatherResponse.daily.
        val firstDate = forecast.dt
        val firstIcon = forecast.weather.first().icon
         */

        val listToReturn = mutableListOf<SimpleWeatherResponse>()

        val currentWeather = weatherResponse.current
        val currentDate = currentWeather.dt
        val currentIcon = currentWeather.weather.first().icon
        listToReturn.add(
            SimpleWeatherResponse(
                dt = currentDate,
                icon = currentIcon
            )
        )

        val forecast = weatherResponse.daily.map {
            val date = it.dt
            val icon = it.weather.first().icon
            listToReturn.add(
                SimpleWeatherResponse(
                    dt = date,
                    icon = icon
                )
            )
        }

        return listToReturn
    }

    /*
    private fun mapToWeather(weatherToday: SimpleWeatherResponse): Weather {
        var location = Location(62.6259, 7.0867)
        return Weather(location, LocalDate.now(), WeatherType.CLEAR_SKY)
    }
     */
}