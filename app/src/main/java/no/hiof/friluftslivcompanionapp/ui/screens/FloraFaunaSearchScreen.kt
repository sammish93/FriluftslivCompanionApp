package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.data.network.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaSearchScreen(
    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel()
) {
    var locationQuery by remember { mutableStateOf(TextFieldValue()) }
    var birdResults = remember {mutableStateOf<List<String>>(emptyList())}
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // TextField for manual location input
        TextField(
            value = locationQuery,
            onValueChange = {
                locationQuery = it
            },
            label = { Text("Enter location") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .onKeyEvent {
                    onKeyEvent(it, locationQuery, viewModel) { birds ->
                        birdResults.value = birds
                    }
                }
        )

        GoogleMapsView { latLng ->
            selectedLocation = latLng
        }
        BirdObservationList(birdResults)

        Text(
            text = "This is the Search By $searchBy tab inside the Search screen!",
            style = CustomTypography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxSize()
        )

        if (selectedLocation != null) {
            CallBirdAPI(selectedLocation!!)
        }
    }
}

@Composable
fun GoogleMapsView(onLocationSelected: (LatLng) -> Unit) {
    val oslo = LatLng(59.9139, 10.7522)

    // Remember the camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(0.dp),
        cameraPositionState = cameraPositionState,
        onMapClick = {latLng -> onLocationSelected (latLng) }

    ) {
        Marker(
            state = MarkerState(position = oslo),
            title = "Oslo",
            snippet = "Marker in Oslo"
        )
    }
}


private fun onKeyEvent(
    event: KeyEvent,
    locationQuery: TextFieldValue,
    viewModel: FloraFaunaViewModel,
    updateResult: (List<String>) -> Unit
): Boolean {
    if (event.key == Key.Enter && locationQuery.text.isNotBlank()) {
        // Launch a coroutine scope to call the suspend function
        CoroutineScope(Dispatchers.Main).launch {
            val api = BirdObservations.getInstance()
            val result = api.getRecentObservations(regionCode = locationQuery.text)
            if (result is Result.Success) {
                println("Recent observations: ${result.value}")
                val birdList = result.value
                val processedList = api.processBirdList(birdList) { bird ->
                    bird.speciesName ?: "Unknown species"
                }
                updateResult(processedList)
            } else if (result is Result.Failure) {
                println("API call failed: ${result.message}")
            }

        }
        return true
    }
    return false
}



@Composable
fun BirdObservationList(birdResults: MutableState<List<String>>) {
    val observations = birdResults.value
    if (observations.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Bird Observations:",
                style = CustomTypography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display each bird observation in the list
            observations.forEach { birdObservation ->
                Text(
                    text = birdObservation,
                    style = CustomTypography.headlineLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}
@Composable
fun CallBirdAPI(selectedLocation: LatLng) {
    val birdResults = remember { mutableStateOf<List<String>>(emptyList()) }

    // Kall BirdAPI med valgt stedsinformasjon
    LaunchedEffect(selectedLocation ) {
        val api = BirdObservations.getInstance()
        val result = api.getRecentObservations(
            regionCode = "NO-03"
        )
        if (result is Result.Success) {
            println("Recent observations: ${result.value}")
            val birdList = result.value
            val processedList = api.processBirdList(birdList) { bird ->
                bird.speciesName ?: "Unknown species"
            }
            birdResults.value = processedList
        } else if (result is Result.Failure) {
            println("API call failed: ${result.message}")
        }
    }

    BirdObservationList(birdResults)
}