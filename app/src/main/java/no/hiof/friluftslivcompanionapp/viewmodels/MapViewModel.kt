package no.hiof.friluftslivcompanionapp.viewmodels

import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import no.hiof.friluftslivcompanionapp.models.GoogleMapState
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(): ViewModel() {

    val state: MutableState<GoogleMapState> = mutableStateOf(
        GoogleMapState(lastKnownLocation = null)
    )

    fun updateLocation(location: Location?) {
        state.value = state.value.copy(lastKnownLocation = location)
    }
}