package no.hiof.friluftslivcompanionapp.data.api

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import no.hiof.friluftslivcompanionapp.data.states.PlaceInfoState
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PlacesApi @Inject constructor(private val placesClient: PlacesClient) {
    private var addressList: List<AddressComponent>? = null

    suspend fun fetchPlaceInfo(placeId: String): PlaceInfoState {
        val request = buildPlaceRequest(placeId)

        return suspendCoroutine { continuation ->
            placesClient.fetchPlace(request).addOnSuccessListener { response: FetchPlaceResponse ->
                    addressList = response.place.addressComponents?.asList()
                    val placeInfoState = buildPlaceInfoState(response)
                    continuation.resume(placeInfoState)
                }
                .addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {
                        Log.e("PlaceInfo", "Place not found: ${exception.message}")
                    }
                    continuation.resumeWithException(exception)
                }
        }
    }

    private fun buildPlaceRequest(placeId: String): FetchPlaceRequest {
        val placeFields = listOf(Place.Field.ADDRESS_COMPONENTS, Place.Field.LAT_LNG)
        return FetchPlaceRequest.newInstance(placeId, placeFields)
    }

    private fun buildPlaceInfoState(response: FetchPlaceResponse): PlaceInfoState {
        val city = findSpecificInfo("locality")
        val county = findSpecificInfo("administrative_area_level_1")
        val country = findSpecificInfo("country")
        val coordinates = response.place.latLng
        return PlaceInfoState(city, county, country, coordinates)
    }

    private fun findSpecificInfo(type: String): String {
        return addressList?.find { it.types.contains(type) }?.name ?: ""
    }
}