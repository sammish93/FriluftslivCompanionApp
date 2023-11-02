package no.hiof.friluftslivcompanionapp.ui.screens

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
    val location = geocoder.getFromLocation(
        userState.lastKnownLocation?.latitude ?: DefaultLocation.OSLO.lat,
        userState.lastKnownLocation?.longitude ?: DefaultLocation.OSLO.lon,
        1
    )
    val address = location?.get(0)
    val addressLine = address?.getAddressLine(0)

    val tripsInArea by viewModel.hikes.collectAsState()
    LaunchedEffect(userState) {
        val geoPoint = userState.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
        if (geoPoint != null) {
            viewModel.getTripsNearUsersLocation(geoPoint, 50.0, 5)
        }
    }

    Scaffold(
        // A button that allows the user to click and display the Bottom Sheet.
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Search this area") },
                icon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                onClick = {
                    //TODO Create behaviour to get coordinates to camera's middle position -
                    // e.g. cameraPositionState.projection?.visibleRegion?.latLngBounds?.center
                    // then create a new tripsInArea search.
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { contentPadding ->

        GoogleMapTripStartNodes(
            navController = navController,
            tripsViewModel = viewModel,
            userViewModel = userViewModel,
            trips = tripsInArea,
            modifier.padding(contentPadding)
        )
    }
}

