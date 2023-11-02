package no.hiof.friluftslivcompanionapp.ui.components.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.utils.getCameraPosition
import no.hiof.friluftslivcompanionapp.utils.getLastKnownLocation
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun GoogleMapTripStartNodes(
    navController: NavController,
    tripsViewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    trips: List<Hike>,
    modifier: Modifier = Modifier,
    onSearchAreaRequested: (LatLng) -> Unit) {

    val mapLoadedState = remember { mutableStateOf(false) }
    val bounds = computeBounds(trips)
    val latestBoundsState = rememberUpdatedState(bounds)

    val userState by userViewModel.state.collectAsState()
    val lastKnownLocation = userState.lastKnownLocation
    val userLocation = getLastKnownLocation(lastKnownLocation)

    val mapProperties =
        MapProperties(
            isMyLocationEnabled = userState.isLocationPermissionGranted,
            mapType = MapType.TERRAIN
        )

    val cameraPosition = getCameraPosition(
        userLocation ?: LatLng(
            DefaultLocation.OSLO.lat,
            DefaultLocation.OSLO.lon
        ), if (userState.isLocationPermissionGranted) 12f else 5f
    )

    /*
    val cameraPositionState = rememberCameraPositionState {
        position = (if (latestBoundsState.value != null) getCameraPosition(
            latestBoundsState.value!!.center,
            12f
        ) else null) ?: cameraPosition }
     */
    val cameraPositionState = rememberCameraPositionState()

    fun searchInCurrentMapArea() {
        val currentCameraPosition = cameraPositionState.position.target
        onSearchAreaRequested(currentCameraPosition)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            onMapLoaded = { mapLoadedState.value = true },
            modifier = Modifier.fillMaxWidth(),
            uiSettings = MapUiSettings(zoomControlsEnabled = true, zoomGesturesEnabled = true)
        ) {
            HikerMarker(
                trips = trips,
                tripsViewModel = tripsViewModel,
                navController = navController
            )
        }
        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            text = { Text("Search this area") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(id = R.string.search))
            },
            onClick = {
                searchInCurrentMapArea()
            }
        )
    }

    val initialLoad = remember { mutableStateOf(true) }
    LaunchedEffect(initialLoad.value) {
        if (initialLoad.value) {
            latestBoundsState.value?.let { bounds ->
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                cameraPositionState.move(cameraUpdate)

                initialLoad.value = false
            }
        }
    }
}

@Composable
fun HikerMarker(
    navController: NavController,
    tripsViewModel: TripsViewModel = viewModel(),
    trips: List<Hike>) {

    val context = LocalContext.current
    when (trips.isEmpty()) {
        false -> {
            for (trip in trips) {
                trip.startLat?.let { lat ->
                    trip.startLng?.let { lng ->
                        val tripStartPosition = LatLng(lat, lng)
                        Marker(
                            MarkerState(position = tripStartPosition),
                            icon = BitmapDescriptorFactory.fromBitmap(TripFactory.changeIconColor(
                                context, R.drawable.baseline_hiking_black_36, trip.difficulty!!
                            )),
                            onClick = {
                                tripsViewModel.updateSelectedTrip(trip)
                                navController.navigate(Screen.TRIPS_ADDITIONAL_INFO.name)
                                true
                            }
                        )
                    }
                }
            }
        }
        true -> {}
    }
}

fun computeBounds(trips: List<Hike>): LatLngBounds? {
    val builder = LatLngBounds.builder()
    if (trips.isNotEmpty()) {
        trips.forEach { trip ->
            trip.startLat?.let { lat ->
                trip.startLng?.let { lng ->
                    builder.include(LatLng(lat, lng))
                }
            }
        }

        return builder.build()
    }

    return null
}



