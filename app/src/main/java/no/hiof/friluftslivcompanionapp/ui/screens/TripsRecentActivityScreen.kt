package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.DummyTrip
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.utils.getCameraPosition
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import java.util.Locale


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
fun TripCard(trip: DummyTrip) {

    var showDialog by remember { mutableStateOf(false) }
    val imageResource = when (trip.type.lowercase(Locale.ROOT)) {
        "hike" -> R.drawable.hike
        "climb" -> R.drawable.climb
        else -> R.drawable.ski
    }

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
        ){
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
                    text = "${trip.type} in ${trip.city}",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "County: ${trip.county}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val lightGreen = 0xFF88E088
                    Text(
                        text = "Distance: ${trip.distance}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier.offset(y = ((-5).dp)),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp
                        ),
                        onClick = { showDialog = true },
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
                GoogleMapWithNodes(nodes = trip.nodes)
            }
        }
    }
}

@Composable
fun GoogleMapWithNodes(nodes: List<LatLng>) {

    val mapProperties = MapProperties(
        isMyLocationEnabled = false,
        mapType = MapType.TERRAIN
    )

    val cameraPosition = getCameraPosition(
        nodes.firstOrNull()
            ?: LatLng(DefaultLocation.OSLO.lat, DefaultLocation.OSLO.lon), 14f
    )
    val cameraPositionState = rememberCameraPositionState { position = cameraPosition }

    GoogleMap(
        modifier = Modifier.fillMaxWidth(),
        properties = mapProperties,
        cameraPositionState = cameraPositionState
    ) {
        nodes.forEach { node ->
            Marker(
                MarkerState(position = node),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                title = "Node",
                snippet = "$node"
            )

            if (nodes.size >= 2) {
                Polyline(
                    points = nodes,
                    color = Color.Red,
                    width = 5f
                )
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

