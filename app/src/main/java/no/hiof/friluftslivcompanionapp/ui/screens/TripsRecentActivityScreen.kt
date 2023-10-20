package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import no.hiof.friluftslivcompanionapp.models.DummyTrip
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel


@Composable
fun TripsRecentActivityScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel()
) {
    val dummyTrips: List<DummyTrip> = DummyTrip.getDummyData()



}

@Composable
fun TripsDetailScreen(trip: DummyTrip) {
    Column {
        Text(text = trip.city)
        Text(text = trip.county)
        Text(text = trip.type)
        Text(text = trip.description)
        Text(text = trip.duration)
        Text(text = "${trip.difficulty} / 5 Difficulty")

        // Må implementere en map veiw.
    }
}

@Composable
fun MapView(nodes: List<LatLng>) {

    GoogleMap() {

    }
}

@Composable
fun TripCard(trip: DummyTrip, onClick: (DummyTrip) -> Unit) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { onClick(trip) })
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ){

        }

    }
}

