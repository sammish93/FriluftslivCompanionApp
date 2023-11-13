package no.hiof.friluftslivcompanionapp.ui.components.maps

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import no.hiof.friluftslivcompanionapp.domain.LocationFormatter
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
    onSearchAreaRequested: (LatLng) -> Unit
) {

    val cameraPositionState = rememberCameraPositionState()
    val latestBoundsState = rememberUpdatedState(computeBounds(trips))
    val userState by userViewModel.state.collectAsState()
    val context = LocalContext.current

    val lastKnownLocation = userState.lastKnownLocation
    val userLocation = getLastKnownLocation(lastKnownLocation)

    // Code inspired by ChatGPT V3.5
    // Retrieves a boolean value as to whether the user currently has internet connectivity.
    val connectivityManager = remember { context.getSystemService(ConnectivityManager::class.java) }
    val isNetworkAvailable by rememberUpdatedState {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    val cameraPosition = getCameraPosition(
        userLocation ?: LatLng(
            DefaultLocation.OSLO.lat,
            DefaultLocation.OSLO.lon
        ), if (userState.isLocationPermissionGranted) 14f else 5f
    )

    val mapProperties =
        MapProperties(
            isMyLocationEnabled = userState.isLocationPermissionGranted,
            mapType = MapType.TERRAIN
        )

    fun searchInCurrentMapArea() {
        val currentCameraPosition = cameraPositionState.position.target
        onSearchAreaRequested(currentCameraPosition)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            modifier = Modifier.fillMaxWidth(),
            uiSettings = MapUiSettings(zoomControlsEnabled = true, zoomGesturesEnabled = true)
        ) {
            HikerMarker(
                trips = trips,
                tripsViewModel = tripsViewModel,
                navController = navController
            )
        }
        if (isNetworkAvailable()) {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                text = { Text(stringResource(R.string.search_this_area)) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                onClick = {
                    searchInCurrentMapArea()
                    /* TODO: Need to have a state in the viewModel that checks if the search is over
                         or not. That is because 'searchInCurrentMapArea' is an asynchronous operation. */
                    if (trips.isEmpty()) {
                        Toast.makeText(
                            context,
                            R.string.no_trips_in_this_radius,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    if (!isNetworkAvailable()) {
                        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
                    }

                },
            )
        }
    }

    LaunchedEffect(Unit) {
        if (latestBoundsState.value != null) {
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latestBoundsState.value!!, 100)
            cameraPositionState.move(cameraUpdate)
        } else {
            // Behaviour to handle when the user's location doesn't contain any existing routes.
            // Prevents camera defaulting to center of the globe.
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            cameraPositionState.move(cameraUpdate)
        }
    }
}

@Composable
fun HikerMarker(
    navController: NavController,
    tripsViewModel: TripsViewModel = viewModel(),
    trips: List<Hike>
) {

    val context = LocalContext.current
    when (trips.isEmpty()) {
        false -> {
            for (trip in trips) {
                trip.startLat?.let { lat ->
                    trip.startLng?.let { lng ->
                        val tripStartPosition = LatLng(lat, lng)
                        Marker(
                            MarkerState(position = tripStartPosition),
                            icon = BitmapDescriptorFactory.fromBitmap(
                                TripFactory.changeIconColor(
                                    context, R.drawable.icons8_region_48, trip.difficulty!!
                                )
                            ),
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

fun computeBoundsFromSingleLatLng(latLng: LatLng): LatLngBounds? {
    val builder = LatLngBounds.builder()

    builder.include(LatLng(latLng.latitude, latLng.longitude))

    return builder.build()
}



