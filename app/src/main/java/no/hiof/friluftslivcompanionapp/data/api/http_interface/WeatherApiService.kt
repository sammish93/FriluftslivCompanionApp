package no.hiof.friluftslivcompanionapp.data.api.http_interface

import no.hiof.friluftslivcompanionapp.models.api.SimpleWeatherResponse
import no.hiof.friluftslivcompanionapp.models.api.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * `WeatherApiService` is an interface representing the API service for OpenWeatherMaps, providing
 * a method to fetch weather forecasts It is intended to be used with a Retrofit instance
 * to create the actual implementation of the API service.
 */
interface WeatherApiService {

    // Appends the following to the end of the URL:
    // ?lat={lat}&lon={lon}&exclude={exclude}&units={metric}&appid={appid}
    @GET("/data/3.0/onecall")
    suspend fun getWeatherToday(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("appid") appid: String
    ): Response<WeatherResponse>
}