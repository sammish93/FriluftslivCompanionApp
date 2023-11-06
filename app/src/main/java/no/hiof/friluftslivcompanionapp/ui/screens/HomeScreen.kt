package no.hiof.friluftslivcompanionapp.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.ui.components.Carousel
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.SnackbarWithCondition
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel = viewModel(),
    tripsViewModel: TripsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Sensors(userViewModel)
    val userLocation by userViewModel.state.collectAsState()
    val hikes by tripsViewModel.hikes.collectAsState()
    val tripsState by tripsViewModel.tripsState.collectAsState()
    val error = tripsViewModel.errorMessage.value
    val locPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val snackbarHostState = remember { SnackbarHostState() }

    if (error != null) {
        Snackbar(modifier = Modifier.padding(16.dp)) {
            Text(text = error)
        }
    }

    LaunchedEffect(userLocation) {
        val geoPoint = userLocation.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
        if (geoPoint != null) {
            tripsViewModel.getTripsNearUsersLocation(geoPoint, radiusInKm = 50.0, limit = 5)
        }
    }

    val currentPage = remember { mutableIntStateOf(0) }

    when (tripsState.isLoading) {
        true -> CustomLoadingScreen()
        else -> when (tripsState.isFailure || !locPermissionState.status.isGranted) {
            false -> {
                Column(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (hikes.isNullOrEmpty()) {
                        Text(
                            text = "There are currently no trips in your area",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "Trips in your area",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Medium,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Carousel(
                            trips = hikes,
                            currentPage = currentPage
                        )
                    }
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
                        text = if (!locPermissionState.status.isGranted) stringResource(R.string.error_no_gps_location_found)
                        else stringResource(
                            R.string.error_retrieving_api_success_response
                        ),
                        style = CustomTypography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = modifier.wrapContentSize(Alignment.Center)
                    )
                    IconButton(onClick = {
                        tripsViewModel.viewModelScope.launch {
                            //TODO Add functionality to prompt the user to share their location if
                            // permissions aren't currently given.
                            val geoPoint = userLocation.lastKnownLocation?.let {
                                GeoPoint(it.latitude, it.longitude)
                            }
                            if (geoPoint != null) {
                                tripsViewModel.getTripsNearUsersLocation(geoPoint, radiusInKm = 50.0, limit = 5)
                            }
                        }
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.refresh)
                        )
                    }

                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp)
                ) {
                SnackbarHost(hostState = snackbarHostState,  modifier = Modifier.align(Alignment.BottomCenter))

                SnackbarWithCondition(
                    snackbarHostState = snackbarHostState,
                    message = (stringResource(R.string.not_share_location_msg_HomePage)),
                    actionLabel = stringResource(R.string.understood),
                    condition = !locPermissionState.status.isGranted

                )
                }
            }
        }

    }
}

