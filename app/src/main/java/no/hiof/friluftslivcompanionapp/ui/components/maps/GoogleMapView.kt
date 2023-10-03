package no.hiof.friluftslivcompanionapp.ui.components.maps

import androidx.compose.foundation.layout.fillMaxWidth
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
fun GoogleMapView() {

    val oslo = LatLng(59.9139, 10.7522)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 10f)
    }
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 70.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = oslo),
            title = "Oslo",
            snippet = "Marker in Oslo"
        )
    }
}