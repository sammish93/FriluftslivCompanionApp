package no.hiof.friluftslivcompanionapp.domain
import no.hiof.friluftslivcompanionapp.BuildConfig
import no.hiof.friluftslivcompanionapp.data.api.GeocodingApi


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
    fun getRegionCode(latitude: Double, longitude: Double): String? {
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


/*
object Geocoding {
    private val geocodingApi = GeocodingApi()
    private val geocodingApiService = geocodingApi.getApiService()

    fun getRegionCode(latitude: Double, longitude: Double): String? {
        val latlng = "$latitude,$longitude"
        val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY

        val response = geocodingApiService.getAddressFromCoordinates(latlng, apiKey).execute()
        if (response.isSuccessful) {
            val geocodeResponse = response.body()
            geocodeResponse?.let { response ->
                // Filter only results for Norway
                val norwayResults = response.results.filter {
                    it.address_components.any { component ->
                        component.types.contains("country") && component.short_name == "NO"
                    }
                }

                // Check if there are results for Norway
                if (norwayResults.isNotEmpty()) {
                    // Extract the administrative area level 1 (region) short name
                    val regionCode = norwayResults.firstOrNull()
                        ?.address_components
                        ?.firstOrNull { it.types.contains("administrative_area_level_1") }
                        ?.short_name

                    // Format the region code as 'NO-XX'
                    return "NO-$regionCode"
                }
            }
        }
        return null
    }
}


 */
