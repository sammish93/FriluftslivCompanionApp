package no.hiof.friluftslivcompanionapp.data.api.http_interface

import no.hiof.friluftslivcompanionapp.models.api.SimpleWeatherResponse
import no.hiof.friluftslivcompanionapp.models.api.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * `EBirdApiService` is an interface representing the API service for eBird, providing
 * a method to fetch recent bird observations. It is intended to be used with a Retrofit instance
 * to create the actual implementation of the API service.
 */
interface WeatherApiService {

    // Appends this to the end of the URL ?lat={lat}&lon={lon}&units=metric&appid={appid}
    @GET("/data/3.0/onecall")
    suspend fun getWeatherToday(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String
    ): Response<WeatherResponse>
}