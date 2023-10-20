package no.hiof.friluftslivcompanionapp.domain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.BuildConfig
import no.hiof.friluftslivcompanionapp.data.api.GeocodingApi


class Geocoding private constructor() {
    private val geocodingApi = GeocodingApi()
    private val geocodingApiService = geocodingApi.getApiService()

    companion object {
        private var instance: Geocoding? = null

        fun getInstance(): Geocoding {
            return instance ?: synchronized(this) {
                instance ?: Geocoding().also { instance = it }
            }
        }
    }

    suspend fun getRegionCode(latitude: Double?, longitude: Double?): String? {
        return withContext(Dispatchers.IO) {
            val latlng = "$latitude,$longitude"
            val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY
            val componentsFilter = "country:NO"
            val response = geocodingApiService.getAddressFromCoordinates(latlng, apiKey, componentsFilter).execute()

            if (response.isSuccessful) {
                val geocodeResponse = response.body()
                geocodeResponse?.let { response ->
                    val addressComponents = response.results.firstOrNull()?.address_components
                    val regionCode = addressComponents?.firstOrNull {
                        it.types.contains("administrative_area_level_1")
                    }?.short_name
                    return@withContext "NO-$regionCode"
                }
            }
            return@withContext null
        }
    }
}

/*
object Geocoding {
    private val geocodingApi = GeocodingApi()
    private val geocodingApiService = geocodingApi.getApiService()

    /**
     * Retrieves the region code based on latitude and longitude using the Geocoding API.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return The region code (ISO 3166-2 format, e.g., "NO-01") for the given location,
     *         or null if the region code could not be retrieved.
     */


    fun getRegionCode(latitude: Double?, longitude: Double?): String? {
        val latlng = "$latitude,$longitude"
        val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY

        val componentsFilter = "country:NO"
        val response = geocodingApiService.getAddressFromCoordinates(latlng, apiKey,componentsFilter).execute()
        if (response.isSuccessful) {
            val geocodeResponse = response.body()
            geocodeResponse?.let { response ->
                val addressComponents = response.results.firstOrNull()?.address_components
                val regionCode = addressComponents?.firstOrNull {
                    it.types.contains("administrative_area_level_1")
                }?.short_name
                return "NO-$regionCode"
            }
        }
        return null
    }
}
*/



