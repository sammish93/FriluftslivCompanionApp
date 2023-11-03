package no.hiof.friluftslivcompanionapp.ui.components.maps

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.utils.getCameraPosition

@Composable
fun GoogleMapTripAdditionalInfo(nodes: List<LatLng>) {

    val mapProperties = MapProperties(
        isMyLocationEnabled = false,
        mapType = MapType.TERRAIN
    )

    val cameraPosition = getCameraPosition(
        nodes.firstOrNull()
            ?: LatLng(DefaultLocation.OSLO.lat, DefaultLocation.OSLO.lon), 14f
    )
    val cameraPositionState = rememberCameraPositionState { position = cameraPosition }

    GoogleMap(
        modifier = Modifier.fillMaxWidth(),
        properties = mapProperties,
        cameraPositionState = cameraPositionState
    ) {
        nodes.forEachIndexed { index, node ->
            Marker(
                MarkerState(position = node),
                icon = if (index == 0) BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) else BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                title = "Node",
                snippet = "$node"
            )

            if (nodes.size >= 2) {
                Polyline(
                    points = nodes,
                    color = Color.Red,
                    width = 5f
                )
            }
        }
    }
}