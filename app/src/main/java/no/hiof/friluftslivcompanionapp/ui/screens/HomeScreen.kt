package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    val hikes by tripsViewModel.hikes.collectAsState()
    val error = tripsViewModel.errorMessage.value

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

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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