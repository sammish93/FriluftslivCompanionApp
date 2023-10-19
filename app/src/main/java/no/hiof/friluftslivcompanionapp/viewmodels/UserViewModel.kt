package no.hiof.friluftslivcompanionapp.viewmodels

import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.data.api.PlacesApi
import no.hiof.friluftslivcompanionapp.data.states.AutoCompleteState
import no.hiof.friluftslivcompanionapp.data.states.PlaceInfoState
import no.hiof.friluftslivcompanionapp.data.states.UserState
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
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
    private val placesApi: PlacesApi,
    private val placesClient: PlacesClient
): ViewModel() {

    private val _state = MutableStateFlow(UserState(lastKnownLocation = null))
    val state: StateFlow<UserState> = _state.asStateFlow()

    // Used for Places API.
    val locationAutoFill = mutableStateListOf<AutoCompleteState>()

    private val _placeInfoState = MutableStateFlow<PlaceInfoState?>(null)
    private val placeInfoState: StateFlow<PlaceInfoState?> = _placeInfoState

    // Used to get city, county, country and coordinates.
    fun fetchPlaceInfo(placeId: String) {
        viewModelScope.launch {
            try {
                val placeInfo = placesApi.fetchPlaceInfo(placeId)
                _placeInfoState.value = placeInfo

                // Just for testing.
                logPlaceInformation(placeInfoState)

            } catch (e: Exception) {
                Log.i("PlaceInfo", "Could not find the place: ${e.message}")
            }
        }
    }

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
        val bounds = RectangularBounds.newInstance(
            LatLng(58.454924, 6.291027),
            LatLng(63.504264, 11.674327)
        )

        return FindAutocompletePredictionsRequest.builder()
            .setLocationBias(bounds)
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
        }
    }

    private fun logPlaceInformation(info: StateFlow<PlaceInfoState?>) {
        Log.i("PlaceInfo", "City: ${info.value?.city}")
        Log.i("PlaceInfo", "County: ${info.value?.county}")
        Log.i("PlaceInfo", "Country: ${info.value?.country}")
        Log.i("PlaceInfo", "Coordinates: ${info.value?.coordinates}")
    }

}