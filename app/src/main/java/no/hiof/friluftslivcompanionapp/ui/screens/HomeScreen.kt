package no.hiof.friluftslivcompanionapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.GeoPoint
import no.hiof.friluftslivcompanionapp.ui.components.Carousel
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun HomeScreen(
    userViewModel: UserViewModel = viewModel(),
    tripsViewModel: TripsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Sensors(userViewModel)

    val userLocation by userViewModel.state.collectAsState()
    
    LaunchedEffect(userLocation) {
        val geoPoint = userLocation.lastKnownLocation?.let { GeoPoint(it.latitude, it.longitude) }
        if (geoPoint != null) {
            tripsViewModel.getTripsNearUsersLocation(geoPoint, radiusInKm = 50.0, limit = 5)
        }
    }
    
    val hikes by tripsViewModel.hikes.collectAsState()
    val updatedHikes = rememberUpdatedState(hikes)
    LaunchedEffect(updatedHikes) {
        Log.d("HomeScreen", "Hikes updated: $updatedHikes")
    }
    
    Column {
        Text("Trips in your area")
        Carousel(trips = hikes)
    }
}