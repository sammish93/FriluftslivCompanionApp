package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.data.api.WeatherApi
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.WeatherForecast
import no.hiof.friluftslivcompanionapp.models.api.SimpleWeatherResponse

/**
 * The `WeatherDeserialiser` class serves as a domain layer in the application architecture,
 * designed as a Singleton to ensure a single instance in the application.
 * It acts as an intermediary between the data layer (`WeatherApi`) and the presentation layer.
 * It is responsible for invoking methods from the data layer to fetch data, which then
 * can be passed to the presentation layer.
 *
 * To get an instance of this class, use the `getInstance` method.
 */
class WeatherDeserialiser private constructor() {

    private val weatherApi = WeatherApi()

    companion object {

        @Volatile
        private var instance: WeatherDeserialiser? = null

        fun getInstance(): WeatherDeserialiser {
            return instance ?: synchronized(this) {
                instance ?: WeatherDeserialiser().also { instance = it }
            }
        }
    }

    /**
     * This method creates an instance of `WeatherApi` and calls the `getWeatherInfo`
     * method on it, passing the longitude, latitude, excluded results, and desired measurement
     * units as parameters.
     *
     * @param lat The latitude coordinates of the location.
     * @param lon The longitude coordinates of the location.
     * @param exclude The weather forecasts that should be excluded from the API result. By default
     * the returned response excludes minutely and hourly forecasts. There is currently no
     * deserialisation functionality in place to retrieve minutely and hourly forecasts.
     * @param units The API returns wind speed in meters/s and temperature in celsius.
     * Alternatives are:
     * Default (empty value "") - wind speed (meters/s), temperature (kalvins).
     * "imperial" - wind speed (miles/h), temperature (farenheit).
     * @return A `WeatherForecast` object containing a 'Location' object representing the area of
     * the weather, along with 9 instances of 'Weather'. The first instance at index 0 is the
     * current weather. Index 1 is a summary of today's weather, and index 2-8 are summaries of the
     * following 7 days.
     */
    suspend fun getWeatherForecast(
        lat: Double,
        lon: Double,
        exclude: String = "minutely,hourly",
        units: String = "metric"
    ): WeatherForecast? {

        val result = weatherApi.getWeatherInfo(lat, lon, exclude, units)

        return when (result) {
            is Result.Success -> {
                val weatherResponseList = result.value
                if (weatherResponseList.isNotEmpty()) {
                    val weatherForecastList = mutableListOf<Weather>()

                    // Iterates over each forecast and creates a separate Weather object which
                    // is then added to a MutableList.
                    weatherResponseList.forEach { weatherForecastList.add(deserialiseWeather(it)) }

                    // Creates a WeatherForecast object
                    val weatherForecast = WeatherForecast(Location(lat, lon), weatherForecastList)

                    return weatherForecast
                } else {
                    // Returns a null object if something went wrong. The stack trace of the
                    // WeatherApi can provide more comprehensive debugging information.
                    null
                }
            }

            else -> {
                null
            }
        }
    }

    /**
     * This method creates an instance of `WeatherApi` and calls the `getWeatherInfo`
     * method on it, passing the longitude, latitude, excluded results, and desired measurement
     * units as parameters.
     *
     * @param lat The latitude coordinates of the location.
     * @param lon The longitude coordinates of the location.
     * @param exclude The weather forecasts that should be excluded from the API result. By default
     * the returned response excludes minutely and hourly forecasts. There is currently no
     * deserialisation functionality in place to retrieve minutely and hourly forecasts.
     * @param units The API returns wind speed in meters/s and temperature in celsius.
     * Alternatives are:
     * Default (empty value "") - wind speed (meters/s), temperature (kalvins).
     * "imperial" - wind speed (miles/h), temperature (farenheit).
     * @return A `Weather` object containing the location, date, wind speed, temperature, and
     * weather type of the current weather.
     */
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        exclude: String = "minutely,hourly",
        units: String = "metric"
    ): Weather? {

        val result = weatherApi.getWeatherInfo(lat, lon, exclude, units)

        return when (result) {
            is Result.Success -> {
                val weatherResponseList = result.value
                if (weatherResponseList.isNotEmpty()) {
                    // Returns the first Weather forecast (current weather)
                    return deserialiseWeather(weatherResponseList.first())
                } else {
                    // Returns a null object if something went wrong. The stack trace of the
                    // WeatherApi can provide more comprehensive debugging information.
                    null
                }
            }

            else -> {
                null
            }
        }
    }

    private fun deserialiseWeather(simpleWeatherResponse: SimpleWeatherResponse): Weather {

        val location = Location(simpleWeatherResponse.lat, simpleWeatherResponse.lon)
        val date = DateFormatter.formatFromUnixTimestamp(simpleWeatherResponse.dt)
        val weatherType = WeatherIconMapper.mapIconToEnum(simpleWeatherResponse.icon)

        val weather = Weather(
            location,
            date,
            simpleWeatherResponse.windSpeed,
            simpleWeatherResponse.temp,
            weatherType
        )

        return weather
    }
}
