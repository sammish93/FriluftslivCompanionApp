package no.hiof.friluftslivcompanionapp.ui.components.items

import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import java.util.Locale

@Composable
fun RecentActivityListItem(recentActivity: TripActivity) {
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val location = geocoder.getFromLocation(
        recentActivity.trip.route.first().latitude,
        recentActivity.trip.route.first().longitude,
        1
    )

    val municipality = location?.firstOrNull()?.subAdminArea ?: "Unkown Location"
    val county = location?.firstOrNull()?.adminArea ?: "Unknown Location"

    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(200.dp)
            .aspectRatio(0.69f),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ){
        Column {
                Text(
                    text = TripType.HIKE.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()

                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = recentActivity.trip.description?: "",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$municipality, $county",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = DateFormatter.formatDate(recentActivity.date),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                )



        }

    }

}

