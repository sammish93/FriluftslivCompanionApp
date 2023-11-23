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
        // We exclude the minutely and hourly forecasts.
        exclude: String = "minutely,hourly",
        // The API returns wind speed in meters/s and temperature in celsius.
        // Alternatives are:
        // Default (no value) - wind speed (meters/s), temperature (kalvins)
        // "imperial" - wind speed (miles/h), temperature (farenheit)
        units: String = "metric"
    ): Result<List<SimpleWeatherResponse>> {
        // Retrieves the API key from an environment variable.
        val apiKey: String = BuildConfig.WEATHER_API_KEY

        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApiService.getWeather(lat, lon, exclude, units, apiKey)
                handleResponse(response, lat, lon)
            } catch (e: Exception) {
                Result.Failure(e.message ?: "Unknown failure")
            }
        }
    }

    // This method handles the response from the OpenWeatherMaps API.
    // It checks if the response is successful and returns a Result object accordingly.
    private fun handleResponse(response: Response<WeatherResponse>, lat: Double, lon: Double): Result<List<SimpleWeatherResponse>> {
        return if (response.isSuccessful) {
            val weatherResponse = response.body()
            weatherResponse?.let { Result.Success(mapToSimpleResponse(it, lat, lon)) }
                ?: Result.Failure("No body in response")
        } else {
            Result.Failure("Unsuccessful response: ${response.errorBody()?.string()}")
        }
    }

    // This method maps a WeatherResponse object to several SimpleWeatherResponse objects, and
    // returns them as a list.
    private fun mapToSimpleResponse(weatherResponse: WeatherResponse, lat: Double, lon: Double): List<SimpleWeatherResponse> {

        val listToReturn = mutableListOf<SimpleWeatherResponse>()

        // Retrieves the current weather
        val currentWeather = weatherResponse.current
        val currentDate = currentWeather.dt
        val currentWindSpeed = currentWeather.wind_speed
        val currentTemp = currentWeather.temp
        val currentIcon = currentWeather.weather.first().icon

        // Adds the current weather in index 0
        listToReturn.add(
            SimpleWeatherResponse(
                lat = lat,
                lon = lon,
                dt = currentDate,
                windSpeed = currentWindSpeed,
                temp = currentTemp,
                icon = currentIcon
            )
        )

        // Retrieves the forecast for today and the following 7 days (summarised).
        val forecast = weatherResponse.daily.map {
            val date = it.dt
            val windSpeed = it.wind_speed
            val temp = it.temp.day
            val icon = it.weather.first().icon
            listToReturn.add(
                SimpleWeatherResponse(
                    lat = lat,
                    lon = lon,
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