package no.hiof.friluftslivcompanionapp.ui.screens

import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.DummyTrip
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.SnackbarWithCondition
import no.hiof.friluftslivcompanionapp.ui.components.cards.TripCard
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel


val dummyTrips: List<DummyTrip> = DummyTrip.getDummyData()
@OptIn(ExperimentalPermissionsApi::class)

@Composable
fun TripsRecentActivityScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    tripsViewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val userState by userViewModel.state.collectAsState()
    val tripsInArea by tripsViewModel.hikes.collectAsState()
    val tripsState by tripsViewModel.tripsState.collectAsState()
   // val locPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    //val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    // Code inspired by ChatGPT V3.5
    // Retrieves a boolean value as to whether the user currently has internet connectivity.
    val connectivityManager = remember { context.getSystemService(ConnectivityManager::class.java) }
    val isNetworkAvailable by rememberUpdatedState {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    LaunchedEffect(userState) {
        val geoPoint = userState.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
        if (geoPoint != null) {
            tripsViewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
        }
    }

    when {
        tripsState.isLoading -> CustomLoadingScreen()
        tripsState.isFailure || !isNetworkAvailable() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (tripsState.isFailure) {
                    Text(
                        text = stringResource(R.string.error_retrieving_api_success_response),
                        style = CustomTypography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = modifier.wrapContentSize(Alignment.Center)
                    )
                }
                if (!isNetworkAvailable()) {
                    Text(
                        text = stringResource(R.string.no_internett_connection),
                        style = CustomTypography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = modifier.wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(tripsInArea) { trip ->
                    //TODO send RecentActivity to trip instead of dummy data.
                    TripCard(navController, trip, tripsViewModel, userViewModel)
                }
            }
        }
    }
}

/*
@Composable
@Preview
fun TripCardPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    ) {
        items(dummyTrips) { trip ->
            TripCard(trip)
        }
    }
}
 */

