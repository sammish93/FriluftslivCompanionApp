package no.hiof.friluftslivcompanionapp.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.hiof.friluftslivcompanionapp.data.states.GoogleMapState
import javax.inject.Inject

/**
 * A ViewModel responsible for managing and updating the state of the Google Map.
 *
 * This ViewModel holds the state of the map, specifically the last known location of the user.
 * It provides functions to update the map's state based on changes in the user's location.
 *
 * @property state Represents the current state of the Google Map, encapsulated in a [GoogleMapState] object.
 * The state can be observed and updated as needed.
 *
 * @constructor Creates a new instance of the MapViewModel. The initial state sets the last known location to null.
 */
@HiltViewModel
class MapViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(GoogleMapState(lastKnownLocation = null))
    val state: StateFlow<GoogleMapState> = _state.asStateFlow()

    // Updates the last known location in the map's state.
    fun updateLocation(location: Location?) {
        val currentState = _state.value
        _state.value = currentState.copy(lastKnownLocation = location)
    }

    fun updateIsInitiallyNavigatedTo(isInitiallyNavigatedTo: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isInitiallyNavigatedTo = isInitiallyNavigatedTo
            )
        }
    }
}