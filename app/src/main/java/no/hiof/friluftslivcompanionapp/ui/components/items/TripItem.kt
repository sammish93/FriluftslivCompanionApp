package no.hiof.friluftslivcompanionapp.ui.components.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.models.Hike

@Composable
fun TripItem(trip: Hike) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Description: ${trip.description ?: "No description available"}")
            Spacer(modifier = Modifier.height(8.dp))
            trip.duration?.let {
                Text(text = "Duration: ${it.toHours()} hours")
            }
            Spacer(modifier = Modifier.height(8.dp))
            trip.distanceKm?.let {
                Text(text = "Distance: $it km")
            }
            Spacer(modifier = Modifier.height(8.dp))
            trip.difficulty?.let {
                Text(text = "Difficulty: ${TripFactory.convertTripDifficultyFromIntToString(it)}")
            }
        }
    }
}