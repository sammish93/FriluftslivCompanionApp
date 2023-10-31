package no.hiof.friluftslivcompanionapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.Hike

@Composable
fun TripItem(trip: Hike) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(480.dp)
            .aspectRatio(0.59f),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.hike),
                contentDescription = "Hiking Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "${trip.description?.replaceFirstChar { it.uppercase() }}",
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                val roundedDistance = String.format("%.1f", trip.distanceKm).toFloat()
                Text(
                    text = "Distance: $roundedDistance km",
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
                Text(
                    text = "Duration: ${trip.duration?.toMinutes()} min",
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                )
            }

        }
    }
}
