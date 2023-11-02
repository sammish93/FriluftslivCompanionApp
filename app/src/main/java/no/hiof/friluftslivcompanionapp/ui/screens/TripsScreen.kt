package no.hiof.friluftslivcompanionapp.ui.screens

import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.GeoPoint
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.UserState
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapTripStartNodes
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.util.Locale

@Composable
fun TripsScreen(
    navController: NavController, modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel
) {
    val userState by userViewModel.state.collectAsState()

    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val location = getLocation(userState, geocoder)

    val tripsInArea by viewModel.hikes.collectAsState()
    LaunchedEffect(userState) {
        val geoPoint = userState.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
        if (geoPoint != null) {
            viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
        }
    }

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

fun getLocation(userState: UserState, geocoder: Geocoder): List<Address>? {
    return geocoder.getFromLocation(
        userState.lastKnownLocation?.latitude ?: DefaultLocation.OSLO.lat,
        userState.lastKnownLocation?.longitude ?: DefaultLocation.OSLO.lon,
        1
    )
}

