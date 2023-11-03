package no.hiof.friluftslivcompanionapp.ui.screens

import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.TripsState
import no.hiof.friluftslivcompanionapp.data.states.UserState
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapTripStartNodes
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun TripsScreen(
    navController: NavController, modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel
) {
    val userState by userViewModel.state.collectAsState()
    val tripsInArea by viewModel.hikes.collectAsState()
    val tripState by viewModel.tripsState.collectAsState()

    LaunchedEffect(Unit) {
        val geoPoint = userState.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
        if (geoPoint != null) {
            viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
        }
    }
    when (tripState.isLoading) {
        true -> CustomLoadingScreen()
        else -> when (tripState.isFailure || tripState.isNoGps) {
            false -> {
                Scaffold(
                    floatingActionButtonPosition = FabPosition.Center
                ) { contentPadding ->

                    GoogleMapTripStartNodes(
                        navController = navController,
                        tripsViewModel = viewModel,
                        userViewModel = userViewModel,
                        trips = tripsInArea,
                        modifier.padding(contentPadding),
                        onSearchAreaRequested = { latLng ->
                            val geoPoint = GeoPoint(latLng.latitude, latLng.longitude)
                            viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
                        }
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (tripState.isNoGps) stringResource(R.string.error_no_gps_location_found)
                        else stringResource(
                            R.string.error_retrieving_api_success_response
                        ),
                        style = CustomTypography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = modifier.wrapContentSize(Alignment.Center)
                    )
                    IconButton(onClick = {
                        viewModel.viewModelScope.launch {
                            //TODO Add functionality to prompt the user to share their location if
                            // permissions aren't currently given.
                            val geoPoint = userState.lastKnownLocation?.let {
                                GeoPoint(it.latitude, it.longitude)
                            }
                            if (geoPoint != null) {
                                viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
                            }
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = stringResource(id = R.string.refresh))
                    }
                }
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

