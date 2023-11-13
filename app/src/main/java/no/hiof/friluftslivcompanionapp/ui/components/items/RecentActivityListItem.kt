package no.hiof.friluftslivcompanionapp.ui.components.items

import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RecentActivityListItem(recentActivity: TripActivity, width: Dp) {
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val location = geocoder.getFromLocation(
        recentActivity.trip.route.first().latitude, recentActivity.trip.route.first().longitude, 1
    )

    val municipality = location?.firstOrNull()?.subAdminArea ?: "Unkown Location"
    val county = location?.firstOrNull()?.adminArea ?: "Unknown Location"

    Card(
        modifier = Modifier
            .padding(2.dp)
            .width(width)
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Text(
                text = TripType.HIKE.name.lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                style = CustomTypography.bodySmall,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$municipality, $county",
                style = CustomTypography.headlineSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = recentActivity.trip.description ?: "",
                maxLines = 1,
                style = CustomTypography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(4.dp))

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val dateString = dateFormat.format(recentActivity.date)
            Text(
                text = dateString,
                style = CustomTypography.titleSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

