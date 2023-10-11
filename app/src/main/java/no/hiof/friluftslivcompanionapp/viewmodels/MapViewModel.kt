package no.hiof.friluftslivcompanionapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapState
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(): ViewModel() {

    val state: MutableState<GoogleMapState> = mutableStateOf(
        GoogleMapState(lastKnownLocation = null)
    )

    fun getDeviceLocation(fusedLocationProviderClient: FusedLocationProviderClient) {

        // Get the best and recent location of the device, which may be null.
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = state.value.copy(
                        lastKnownLocation = task.result,
                    )
                }
            }
        } catch (e: SecurityException) {
            // Show error or something.
        }

    }
}