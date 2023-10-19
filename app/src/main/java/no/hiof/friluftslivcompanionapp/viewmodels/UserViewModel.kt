package no.hiof.friluftslivcompanionapp.viewmodels

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.hiof.friluftslivcompanionapp.data.states.AutoCompleteState
import no.hiof.friluftslivcompanionapp.data.states.SelectedLocationState
import no.hiof.friluftslivcompanionapp.data.states.UserState
import java.lang.Exception
import javax.inject.Inject

/**
 * A ViewModel responsible for managing and updating the state of the Google Map.
 *
 * This ViewModel holds the state of the map, specifically the last known location of the user.
 * It provides functions to update the map's state based on changes in the user's location.
 *
 * @property state Represents the current state of the Google Map, encapsulated in a [UserState] object.
 * The state can be observed and updated as needed.
 *
 * @constructor Creates a new instance of the MapViewModel. The initial state sets the last known location to null.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val placesClient: PlacesClient
): ViewModel() {

    private val _state = MutableStateFlow(UserState(lastKnownLocation = null))
    val state: StateFlow<UserState> = _state.asStateFlow()

    // Used for Places API.
    val locationAutoFill = mutableStateListOf<AutoCompleteState>()

    // Updates the last known location in the map's state.
    fun updateLocation(location: Location?) {
        _state.update { currentState ->
            currentState.copy(
                lastKnownLocation = location
            )
        }
    }

    // Updates the currently logged in user.
    fun updateCurrentUser(currentUser: FirebaseUser) {
        _state.update { currentState ->
            currentState.copy(
                currentUser = currentUser
            )
        }
    }

    // Returns the current user as a FirebaseUser object.
    fun getCurrentUser(): FirebaseUser? {
        return  _state.value.currentUser
    }

    // Updates the status of the location manager loading a user's GPS position.
    fun updateLocationManagerCalled(isLocationManagerCalled: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isLocationManagerCalled = isLocationManagerCalled
            )
        }
    }

    // Updates whether the user has shared their location.
    fun updateLocationPermissionGranted(isLocationPermissionGranted: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isLocationPermissionGranted = isLocationPermissionGranted
            )
        }
    }

    // FUNCTIONS USED FOR PLACES API.
    fun searchPlaces(query: String) {

        // Empty old result.
        locationAutoFill.clear()

        val request = getAutocompleteRequester(query)
        handleAutocompletePrediction(request)
    }

    fun clearAutocompleteResults() {
        locationAutoFill.clear()
    }

    private fun getAutocompleteRequester(query: String): FindAutocompletePredictionsRequest {
        return FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
    }

    private fun handleAutocompletePrediction(request: FindAutocompletePredictionsRequest) {
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            locationAutoFill.addAll(
                response.autocompletePredictions.map {
                    AutoCompleteState(
                        it.getFullText(null).toString(),
                        it.placeId
                    )
                }
            )
        }.addOnFailureListener {
            // Handle errors here.
        }
    }

    fun fetchPlaceInfo(placeId: String) {
        val placeFields = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG
        )

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val addressComponentsList = response.place.addressComponents?.asList()
                val cityComponent = addressComponentsList?.find { it.types.contains("locality") }
                val countyComponent = addressComponentsList?.find { it.types.contains("administrative_area_level_1") }
                val countryComponent = addressComponentsList?.find { it.types.contains("country") }

                val city = cityComponent?.name
                val county = countyComponent?.name
                val country = countryComponent?.name
                val coordinates = response.place.latLng

                Log.i("PlaceInfo",
                    "City: $city, County: $county, Country: $country, " +
                            "Coordinates: ${coordinates?.latitude}, ${coordinates?.longitude}")

            }

            .addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("PlaceInfo", "Place not found: ${exception.message}")
                }
            }
    }

}