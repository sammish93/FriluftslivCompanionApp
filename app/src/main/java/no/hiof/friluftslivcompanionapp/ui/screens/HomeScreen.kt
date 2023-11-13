package no.hiof.friluftslivcompanionapp.ui.screens

import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import no.hiof.friluftslivcompanionapp.ui.components.SnackbarWithCondition
import no.hiof.friluftslivcompanionapp.ui.components.items.BirdItem
import no.hiof.friluftslivcompanionapp.ui.components.items.LifelistItem
import no.hiof.friluftslivcompanionapp.ui.components.items.RecentActivityListItem
import no.hiof.friluftslivcompanionapp.ui.components.items.TripItem
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel

import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    userViewModel: UserViewModel = viewModel(),
    tripsViewModel: TripsViewModel = viewModel(),
    floraFaunaViewModel: FloraFaunaViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    // Code inspired by ChatGPT V3.5
    // Retrieves a boolean value as to whether the user currently has internet connectivity.
    val connectivityManager = remember { context.getSystemService(ConnectivityManager::class.java) }
    val isNetworkAvailable by rememberUpdatedState {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }


    // Sensors(userViewModel)
    val userLocation by userViewModel.state.collectAsState()
    val hikes by tripsViewModel.hikes.collectAsState()
    val tripsState by tripsViewModel.tripsState.collectAsState()

    val lifelist by floraFaunaViewModel.lifeList.collectAsState()
    val recentActivity by tripsViewModel.recentActivity.collectAsState()

    val sightings by floraFaunaViewModel.sightingsFlow.collectAsState()


    val error = tripsViewModel.errorMessage.value
    val locPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val snackbarHostState = remember { SnackbarHostState() }

    val scrollState = rememberScrollState()
    val isTripCarouselQueryCalled = remember { mutableStateOf(false) }

    if (error != null) {
        Snackbar(modifier = Modifier.padding(16.dp)) {
            Text(text = error)
        }
    }

    LaunchedEffect(userLocation) {
        if (!isTripCarouselQueryCalled.value) {
            val geoPoint =
                userLocation.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
            if (geoPoint != null) {
                tripsViewModel.getTripsNearUsersLocation(geoPoint, radiusInKm = 50.0, limit = 5)
                isTripCarouselQueryCalled.value = true

                floraFaunaViewModel.getSightingsNearLocation(geoPoint, 50.0, 5)
            }

            floraFaunaViewModel.getUserLifeList()
            tripsViewModel.getRecentActivity()
        }
    }

    val currentPage = remember { mutableIntStateOf(0) }

    when (tripsState.isFailure || !locPermissionState.status.isGranted || !isNetworkAvailable()) {
        false -> {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.trips_in_your_area),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (hikes.isNullOrEmpty()) {
                    Text(
                        text = stringResource(R.string.there_are_currently_no_trips_in_your_area),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    hikes.let { hikes ->
                        val hikesToDisplay = hikes.take(5)

                        Carousel(items = hikesToDisplay, currentPage = currentPage) { hike ->
                            TripItem(trip = hike)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sightings in your area",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (sightings.isNullOrEmpty()) {
                    Text(
                        text = "There are currently no sightings in your area",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    sightings.let { sightings ->
                        val sightingsToDisplay = sightings.take(5)

                        Carousel(
                            items = sightingsToDisplay,
                            currentPage = currentPage
                        ) { sighting ->
                            BirdItem(sighting = sighting)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.recent_sightings),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (lifelist.isNullOrEmpty()) {
                    Text(
                        text = "You currently have no sightings in your lifelist",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    lifelist?.let { lifeList ->
                        val sightingsToDisplay = lifeList.take(5)

                        Carousel(
                            items = sightingsToDisplay,
                            currentPage = currentPage
                        ) { sighting ->
                            LifelistItem(lifeList = sighting)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.recent_activity),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (recentActivity.isNullOrEmpty()) {
                    Text(
                        text = "You currently have no trips in your trip log",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    recentActivity?.let { activityList ->
                        val limitedActivity = activityList.take(5)

                        Carousel(
                            items = limitedActivity,
                            currentPage = currentPage
                        ) { recentActivity ->
                            RecentActivityListItem(recentActivity = recentActivity)
                        }
                    }
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
                    text = when {
                        !locPermissionState.status.isGranted -> stringResource(R.string.error_no_gps_location_found)
                        !isNetworkAvailable() -> stringResource(R.string.no_internett_connection)
                        else -> stringResource(R.string.error_retrieving_api_success_response)
                    },
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
                            tripsViewModel.getTripsNearUsersLocation(
                                geoPoint,
                                radiusInKm = 50.0,
                                limit = 5
                            )
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
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
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


