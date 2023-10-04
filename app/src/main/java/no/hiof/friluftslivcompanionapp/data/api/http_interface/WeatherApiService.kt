package no.hiof.friluftslivcompanionapp.data.api.http_interface

import no.hiof.friluftslivcompanionapp.models.api.SimpleWeatherResponse
import no.hiof.friluftslivcompanionapp.models.api.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * `EBirdApiService` is an interface representing the API service for eBird, providing
 * a method to fetch recent bird observations. It is intended to be used with a Retrofit instance
 * to create the actual implementation of the API service.
 */
interface WeatherApiService {

    @GET("/data/3.0/onecall?lat={lat}&lon={lon}&units=metric&appid={apiKey}")
    suspend fun getWeatherToday(
        @Path("lat") lat: Double,
        @Path("lon") lon: Double,
        @Path("apiKey") day: String
    ): Response<WeatherResponse>
}