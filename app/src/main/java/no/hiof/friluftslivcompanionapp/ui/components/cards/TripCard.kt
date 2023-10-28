package no.hiof.friluftslivcompanionapp.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.DummyTrip
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Composable
fun TripCard(
    navController: NavController,
    trip: TripActivity,
    tripsViewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {

    //var showDialog by remember { mutableStateOf(false) }
    /*
    val imageResource = when (trip.type.lowercase(Locale.ROOT)) {
        "hike" -> R.drawable.hike
        "climb" -> R.drawable.climb
        else -> R.drawable.ski
    }

     */
    val imageResource = R.drawable.hike
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(275.dp)
            .width(240.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Hiking Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 8.dp)
            ) {
                Text(
                    text = "${trip.trip.description}",
                    style = MaterialTheme.typography.headlineMedium
                )



                Spacer(modifier = Modifier.height(8.dp))
                val dateString = formatDate(trip.date)
                Text(
                    text = "County: $dateString ",
                    style = MaterialTheme.typography.bodyMedium
                )



                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val lightGreen = 0xFF88E088

                    Text(
                        text = "Date:  ",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        modifier = Modifier.offset(y = ((-5).dp)),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp
                        ),
                        onClick = { //showDialog = true
                            //Updates the trip selected in the view model so that the page can
                            // access various trips dynamically.
                           // tripsViewModel.updateSelectedTrip(trip)
                            //tripsViewModel.updateSelectedTrip(trip)
                            navController.navigate(Screen.TRIPS_ADDITIONAL_INFO.name)
                        },
                        colors = ButtonColors(
                            containerColor = Color(lightGreen),
                            contentColor = Color.Black,
                            disabledContainerColor = Color(lightGreen),
                            disabledContentColor = Color.Black
                        )
                    ) {
                        Text(text = "View Trip!")
                    }
                }
            }
        }
    }
    /*
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                GoogleMapTripAdditionalInfo(nodes = trip.nodes)
            }
        }
    }
     */
}

//TODO: Add the formatDate to DateFormatter class
fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(date)
}

