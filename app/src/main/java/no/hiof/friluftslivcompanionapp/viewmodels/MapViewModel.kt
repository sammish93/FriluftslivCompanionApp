package no.hiof.friluftslivcompanionapp.viewmodels

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val state: MutableState<GoogleMapState> = mutableStateOf(
        GoogleMapState(lastKnownLocation = null)
    )

    // Updates the last known location in the map's state.
    fun updateLocation(location: Location?) {
        state.value = state.value.copy(lastKnownLocation = location)
    }
}