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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaSearchScreen(
    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel()
) {
    var locationQuery by remember { mutableStateOf(TextFieldValue()) }
    var birdObservations = remember {BirdObservations.getInstance()}


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
                .onKeyEvent { onKeyEvent(it, locationQuery, birdObservations) }
        )

        GoogleMapsView()

        Text(
            text = "This is the Search By $searchBy tab inside the Search screen!",
            style = CustomTypography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxSize()
        )
    }
}

@Composable
fun GoogleMapsView() {
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
    birdObservations: BirdObservations
): Boolean {
    if (event.key == Key.Tab) {
        // Launch a coroutine scope to call the suspend function
        CoroutineScope(Dispatchers.Main).launch {
            val result = birdObservations.getRecentObservations(regionCode = locationQuery.text)
            // Handle the result as needed
        }
        return true
    }
    return false
}

