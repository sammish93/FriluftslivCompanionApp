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
import no.hiof.friluftslivcompanionapp.utils.getCameraPosition

@Composable
fun GoogleMapTripStartNodes(trips: List<Hike>) {

    val mapLoadedState = remember { mutableStateOf(false) }
    val bounds = computeBounds(trips)
    val latestBoundsState = rememberUpdatedState(bounds)

    val pos = getCameraPosition(latestBoundsState.value.center, 12f)
    val cameraPositionState = rememberCameraPositionState { position = pos }


    GoogleMap(
        cameraPositionState = cameraPositionState,
        onMapLoaded = { mapLoadedState.value = true },
        modifier = Modifier.fillMaxWidth(),
        uiSettings = MapUiSettings(zoomControlsEnabled = false, zoomGesturesEnabled = false)
        ) {
        HikerMarker(trips = trips)
    }

    LaunchedEffect(mapLoadedState.value) {
        if (mapLoadedState.value) {
            val cameraUpdate = latestBoundsState.value.let { CameraUpdateFactory.newLatLngBounds(it, 100) }
            cameraUpdate.let { cameraPositionState.move(it) }
        }
    }
}

@Composable
fun HikerMarker(trips: List<Hike>) {
    for (trip in trips) {
        trip.startLat?.let { lat ->
            trip.startLng?.let { lng ->
                val tripStartPosition = LatLng(lat, lng)
                Marker(
                    MarkerState(position = tripStartPosition),
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.baseline_hiking_black_36),
                    onClick = { TODO() }
                )
            }
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



