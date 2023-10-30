package no.hiof.friluftslivcompanionapp.ui.components.maps

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.Hike

@Composable
fun GoogleMapTripStartNodes(trips: List<Hike>) {

    val bounds = computeBounds(trips)
    val mapReadyState = remember { mutableStateOf(false) }
    val latestBoundsState = rememberUpdatedState(bounds)
    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        cameraPositionState = cameraPositionState,
        onMapLoaded = { mapReadyState.value = true },
        modifier = Modifier.fillMaxWidth(),
        uiSettings = MapUiSettings(zoomControlsEnabled = false, zoomGesturesEnabled = false)
        ) {

        for (trip in trips) {
            trip.startLat?.let { lat ->
                trip.startLng?.let { lng ->
                    val tripStartPosition = LatLng(lat, lng)
                    Marker(
                        MarkerState(position = tripStartPosition),
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_hiking_black_48),
                        onClick = { TODO() }
                    )
                }
            }
        }
    }
    LaunchedEffect(mapReadyState.value) {
        if (mapReadyState.value) {
            val cameraUpdate =
                latestBoundsState.value.let { CameraUpdateFactory.newLatLngBounds(it, 100) }
            cameraUpdate.let { cameraPositionState.move(it) }
        }
    }
}

fun computeBounds(trips: List<Hike>): LatLngBounds {
    val builder = LatLngBounds.builder()
    trips.forEach { trip ->
        trip.startLat?.let { lat ->
            trip.startLng?.let { lng ->
                builder.include(LatLng(lat, lng))
            }
        }
    }

    return builder.build()
}



