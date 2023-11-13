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
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.Screen
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
    navController: NavController,
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
    val userState by userViewModel.state.collectAsState()
    val hikesState by tripsViewModel.hikes.collectAsState()
    val tripsState by tripsViewModel.tripsState.collectAsState()
    val lifelistState by floraFaunaViewModel.lifeList.collectAsState()
    val recentActivityState by tripsViewModel.recentActivity.collectAsState()
    val sightingsState by floraFaunaViewModel.sightingsFlow.collectAsState()

    val error = tripsViewModel.errorMessage.value
    val locPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val snackbarHostState = remember { SnackbarHostState() }

    val scrollState = rememberScrollState()
    val isTripCarouselQueryCalled = remember { mutableStateOf(false) }

    // Dimensions below are used for responsive design with carousel cards.
    val width = when (userState.windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 200.dp
        WindowWidthSizeClass.Medium -> 300.dp

        else -> 350.dp
    }

    val height = when (userState.windowSizeClass.heightSizeClass) {
        WindowHeightSizeClass.Compact -> 150.dp
        WindowHeightSizeClass.Medium -> 200.dp

        else -> 250.dp
    }

    val aspectRatio = when (userState.windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 1f
        WindowWidthSizeClass.Medium -> 2f

        else -> 2.5f
    }

    if (error != null) {
        Snackbar(modifier = Modifier.padding(16.dp)) {
            Text(text = error)
        }
    }

    LaunchedEffect(userState) {
        if (!isTripCarouselQueryCalled.value) {
            val geoPoint =
                userState.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
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

                if (hikesState.isNullOrEmpty()) {
                    if ((!locPermissionState.status.isGranted || userState.lastKnownLocation == null)) {
                        Text(
                            text = stringResource(R.string.no_trips_can_be_found_in_your_area_because_you_aren_t_currently_sharing_your_location),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.there_are_currently_no_trips_in_your_area),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    hikesState.let { hikes ->
                        val hikesToDisplay = hikes.take(5)

                        Carousel(items = hikesToDisplay, currentPage = currentPage) { hike ->
                            val index = hikesToDisplay.indexOf(hike)

                            TripItem(
                                trip = hike,
                                height = height,
                                aspectRatio = aspectRatio,
                                onClick = {
                                    tripsViewModel.updateSelectedTrip(hike)
                                    navController.navigate(Screen.TRIPS_ADDITIONAL_INFO.name)
                                },
                                tripsViewModel.tripImages[index] ?: R.drawable.oslomarka
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.sightings_in_your_area),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Medium,
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (sightingsState.isNullOrEmpty()) {
                    if ((!locPermissionState.status.isGranted || userState.lastKnownLocation == null)) {
                        Text(
                            text = stringResource(R.string.no_sightings_can_be_found_in_your_area_because_you_aren_t_currently_sharing_your_location),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.there_are_currently_no_sightings_in_your_area),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    sightingsState.let { sightings ->
                        val sightingsToDisplay = sightings.take(5)

                        Carousel(
                            items = sightingsToDisplay,
                            currentPage = currentPage
                        ) { sighting ->
                            BirdItem(sighting = sighting,
                                height = height,
                                aspectRatio = aspectRatio,
                                onClick = {
                                    floraFaunaViewModel.updateSelectedSpeciesInfo(sighting.species)
                                    navController.navigate(Screen.FLORA_FAUNA_ADDITIONAL_INFO.name)
                                })
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

                if (lifelistState.isNullOrEmpty()) {
                    Text(
                        text = stringResource(R.string.you_currently_have_no_sightings_in_your_lifelist),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    lifelistState?.let { lifeList ->
                        val sightingsToDisplay = lifeList.take(5)

                        Carousel(
                            items = sightingsToDisplay,
                            currentPage = currentPage
                        ) { sighting ->
                            LifelistItem(lifeList = sighting,
                                height = height,
                                aspectRatio = aspectRatio,
                                onClick = {
                                    floraFaunaViewModel.updateSelectedSpeciesInfo(sighting.sightings.species)
                                    navController.navigate(Screen.FLORA_FAUNA_ADDITIONAL_INFO.name)
                                })
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

                if (recentActivityState.isNullOrEmpty()) {
                    Text(
                        text = stringResource(R.string.you_currently_have_no_trips_in_your_trip_log),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    recentActivityState?.let { activityList ->
                        val limitedActivity = activityList.take(5)

                        Carousel(
                            items = limitedActivity,
                            currentPage = currentPage
                        ) { recentActivity ->
                            RecentActivityListItem(recentActivity = recentActivity, width = width,
                                onClick = {
                                    tripsViewModel.updateSelectedTrip(recentActivity.trip)
                                    navController.navigate(Screen.TRIPS_ADDITIONAL_INFO.name)
                                })
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
                        !isNetworkAvailable() -> stringResource(R.string.no_internet_connection)
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
                        val geoPoint = userState.lastKnownLocation?.let {
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


