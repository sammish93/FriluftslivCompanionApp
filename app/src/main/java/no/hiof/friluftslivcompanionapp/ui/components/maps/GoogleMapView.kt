package no.hiof.friluftslivcompanionapp.ui.components.maps

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
@Composable
fun GoogleMapsView(onLocationSelected: (LatLng) -> Unit) {
    val oslo = LatLng(59.9139, 10.7522)

    // Remember the camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(0.dp),
        cameraPositionState = cameraPositionState,
        onMapClick = {latLng -> onLocationSelected (latLng) }

    ) {
        Marker(
            state = MarkerState(position = oslo),
            title = "Oslo",
            snippet = "Marker in Oslo"
        )
    }
}