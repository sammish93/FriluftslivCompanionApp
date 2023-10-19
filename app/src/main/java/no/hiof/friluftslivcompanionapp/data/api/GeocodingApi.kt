package no.hiof.friluftslivcompanionapp.data.api;

import no.hiof.friluftslivcompanionapp.data.api.http_interface.GeocodingApiService;
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder


/**
 * The `GeocodingApi` class creates an instance of `GeocodingApiService`.
 * It uses `RetrofitBuilder` to lazily initialize the Geocoding API service, providing a single point
 * of access to the Geocoding API functionality within the application.
 */

class GeocodingApi {
    private val geocodingService: GeocodingApiService by lazy {
        val retrofit = RetrofitBuilder.buildGeocodingApi()
        retrofit.create(GeocodingApiService::class.java)
    }
    fun getApiService(): GeocodingApiService {
        return geocodingService
    }
}
/*
class GeocodingApi private constructor() {
    private val geocodingService: GeocodingApiService by lazy {
        val retrofit = RetrofitBuilder.buildGeocodingApi()
        retrofit.create(GeocodingApiService::class.java)
    }

    companion object {
        private var instance: GeocodingApi? = null

        fun getInstance(): GeocodingApi {
            return instance ?: synchronized(this) {
                instance ?: GeocodingApi().also { instance = it }
            }
        }
    }

    fun getApiService(): GeocodingApiService {
        return geocodingService
    }
}

*/

