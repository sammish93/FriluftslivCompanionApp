package no.hiof.friluftslivcompanionapp.ui.components.maps
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import java.io.IOException

@Composable
fun GoogleMapsView(locationName: String, viewModel: FloraFaunaViewModel) {
    val location = getLocationFromName(locationName, LocalContext.current)
    val defaultLocation = LatLng(59.9139, 10.7522)
    val initialLocation = location ?: defaultLocation
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 230.dp),
        cameraPositionState = cameraPositionState

    ) {
        location?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Location",
                snippet = "Marker at specified location"
            )
        }
    }
}

// Only for testing purposes.
@Composable
fun GoogleMap(state: GoogleMapState) {

    val mapProperties = MapProperties(isMyLocationEnabled = state.lastKnownLocation != null)
    val userLocation = state.lastKnownLocation?.let { LatLng(it.latitude, state.lastKnownLocation.longitude) }

    val defaultCameraPosition = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    val cameraPosition = userLocation?.let { CameraPosition.fromLatLngZoom(it, 10f) } ?: defaultCameraPosition

    val cameraPositionState = rememberCameraPositionState {
        position = cameraPosition
    }
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp),
        properties = mapProperties,
        cameraPositionState = cameraPositionState
    ) {

    }
}



private fun getLocationFromName(locationName: String, context: Context): LatLng? {
    val geocoder = Geocoder(context)
    var addressList: List<Address>? = null

    try {
        // Get the address list based on the location name
        //Should use this instead:
        // public List<Address> getFromLocation (double latitude,
        //                double longitude,
        //                int maxResults)
        addressList = geocoder.getFromLocationName(locationName, 1)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    // Check if we have a valid address
    if (!addressList.isNullOrEmpty()) {
        val address: Address = addressList[0]

        return LatLng(address.latitude, address.longitude)
    }
    return null
}
