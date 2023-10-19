package no.hiof.friluftslivcompanionapp.data.api.http_interface

import no.hiof.friluftslivcompanionapp.models.api.GeocodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for interacting with the Geocoding API.
 * This service allows you to retrieve address information based on geographic coordinates.
 */
interface GeocodingApiService {
    @GET("maps/api/geocode/json")
    fun getAddressFromCoordinates(
        @Query("latlng") latlng: String, // Longitude and latitude as "lat,lng"
        @Query("key") apiKey: String, // API key for accessing the service
        @Query("components") components: String // Component filter

    ): Call<GeocodeResponse>
}