package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.DummyTrip
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel

val dummyTrips: List<DummyTrip> = DummyTrip.getDummyData()

@Composable
fun TripsRecentActivityScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel()
) {


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    ) {
        items(dummyTrips) { trip ->
            TripCard(trip)
        }
    }
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
fun TripCard(trip: DummyTrip) {

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.hike),
                contentDescription = "Hiking Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .fillMaxSize()
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            Spacer(modifier = Modifier.width(49.dp))

            Column(
                modifier = Modifier.weight(1f)
            ){
                Text(text = trip.city, fontWeight = FontWeight.Bold, fontSize = 20.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = trip.county)
                Text(text = trip.type)
                Text(text = trip.distance)
            }
        }
    }
}

@Composable
@Preview
fun TripCardPreview() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    ) {
        items(dummyTrips) { trip ->
            TripCard(trip)
        }
    }
}

