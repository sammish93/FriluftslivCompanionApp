package no.hiof.friluftslivcompanionapp.domain
import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.models.api.SimpleWeatherResponse
import org.junit.Before
import org.junit.Test


import org.junit.Assert

import no.hiof.friluftslivcompanionapp.models.enums.WeatherUnit

class WeatherDeserialiserTest {

    private lateinit var weatherDeserialiser: WeatherDeserialiser

    @Before
    fun setUp() {
        weatherDeserialiser = WeatherDeserialiser.getInstance()
    }

    /**
     * Tests whether the `getWeatherForecast` method in WeatherDeserialiser returns a successful response
     * when called with valid coordinates for Oslo, Norway, and the unit is set to METRIC.
     */
    @Test
    fun testGetWeatherForecast() = runBlocking{
        val lat = 59.9139 // Oslo, Norge
        val lon = 10.7522

        val result = weatherDeserialiser.getWeatherForecast(lat, lon, units = WeatherUnit.METRIC)

        Assert.assertTrue(result is Result.Success)
    }

    /**
     * Tests whether the `getCurrentWeather` method in WeatherDeserialiser returns a successful response
     * when called with valid coordinates for Oslo, Norway, and the unit is set to METRIC.
     */
    @Test
    fun testGetCurrentWeather() = runBlocking{
        val lat = 59.9139 // Oslo, Norge
        val lon = 10.7522

        val result = weatherDeserialiser.getCurrentWeather(lat, lon, units = WeatherUnit.METRIC)

        Assert.assertTrue(result is Result.Success)
    }

    /**
     * Tests whether the `getWeatherForecast` method in WeatherDeserialiser handles invalid coordinates
     * by expecting a Result.Failure as a response.
     */
    @Test
    fun testGetWeatherForecastWithInvalidCoordinates() = runBlocking {
        val lat = 999.999
        val lon = 999.999

        val result = weatherDeserialiser.getWeatherForecast(lat, lon, units = WeatherUnit.METRIC)

        Assert.assertTrue(result is Result.Failure)
    }

    /**
     * Tests whether the `getCurrentWeather` method in WeatherDeserialiser handles invalid coordinates
     * by expecting a Result.Failure as a response.
     */
    @Test
    fun testGetCurrentWeatherWithInvalidCoordinates() = runBlocking {
        val lat = 999.999
        val lon = 999.999

        val result = weatherDeserialiser.getCurrentWeather(lat, lon, units = WeatherUnit.METRIC)

        Assert.assertTrue(result is Result.Failure)
    }
}



