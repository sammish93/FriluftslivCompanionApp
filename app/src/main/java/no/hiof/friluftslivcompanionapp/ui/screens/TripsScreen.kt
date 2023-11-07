package no.hiof.friluftslivcompanionapp.ui.screens

import android.Manifest
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.firestore.GeoPoint
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.UserState
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.ErrorView
import no.hiof.friluftslivcompanionapp.ui.components.SnackbarWithCondition
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapTripStartNodes
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TripsScreen(
    navController: NavController, modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel
) {
    val locPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val snackbarHostState = remember { SnackbarHostState() }

    val userState by userViewModel.state.collectAsState()
    val tripsInArea by viewModel.hikes.collectAsState()
    val tripState by viewModel.tripsState.collectAsState()

    // Retrieves the trips near a user's GPS location in a 50km radius.
    LaunchedEffect(Unit) {
        val geoPoint = userState.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
        if (geoPoint != null) {
            viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
        }
    }
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center
    ) { contentPadding ->
        when {
            tripState.isLoading -> CustomLoadingScreen()

            !locPermissionState.status.isGranted -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    // Displays the map based on a default location.
                    GoogleMapTripStartNodes(
                        navController = navController,
                        tripsViewModel = viewModel,
                        userViewModel = userViewModel,
                        trips = tripsInArea,
                        modifier = Modifier.weight(1f),
                        onSearchAreaRequested = { latLng ->
                                val geoPoint = GeoPoint(latLng.latitude, latLng.longitude)
                                viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
                        }
                    )
                }

                SnackbarWithCondition(
                    snackbarHostState = snackbarHostState,
                    message = stringResource(R.string.not_share_location_msg_TripScreen),
                    actionLabel = stringResource(R.string.understood),
                    condition = !locPermissionState.status.isGranted
                )

                Box{
                    SnackbarHost(hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            tripState.isFailure -> {
                ErrorView(message = stringResource(R.string.error_retrieving_api_success_response))
            }

            else -> {
                // Displays the map based on a user's GPS.
                GoogleMapTripStartNodes(
                    navController = navController,
                    tripsViewModel = viewModel,
                    userViewModel = userViewModel,
                    trips = tripsInArea,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    onSearchAreaRequested = { latLng ->
                        val geoPoint = GeoPoint(latLng.latitude, latLng.longitude)
                        viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
                    }
                )
            }
        }
    }
}


fun getLocation(userState: UserState, geocoder: Geocoder): List<Address>? {
    return geocoder.getFromLocation(
        userState.lastKnownLocation?.latitude ?: DefaultLocation.OSLO.lat,
        userState.lastKnownLocation?.longitude ?: DefaultLocation.OSLO.lon,
        1
    )
}

