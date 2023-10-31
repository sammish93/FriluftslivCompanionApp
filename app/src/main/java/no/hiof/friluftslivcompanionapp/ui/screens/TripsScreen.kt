package no.hiof.friluftslivcompanionapp.ui.screens

import android.location.Geocoder
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.GeoPoint
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

    Box(
        modifier = Modifier
            .padding(top = 24.dp, start = 8.dp, bottom = 24.dp, end = 8.dp)
            .fillMaxSize()
    ) {
        Column {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Trips near:")
                    Text(text = "$addressLine")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.clip(RoundedCornerShape(20.dp)),
                verticalArrangement = Arrangement.Bottom
            ) {
                GoogleMapTripStartNodes(
                    navController = navController,
                    trips = tripsInArea
                )
            }
        }
    }
}

