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
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.ui.components.BirdObservationList
import no.hiof.friluftslivcompanionapp.ui.components.CallBirdAPI
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaSearchScreen(
    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel()
) {
    var locationQuery by remember { mutableStateOf(TextFieldValue()) }
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
                viewModel.searchBirds(it.text)
            },
            label = { Text("Enter location") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .onKeyEvent {
                    onKeyEvent(it, locationQuery, viewModel)
                }
        )
        val birdResults by viewModel.birdResults.collectAsState(emptyList())
        BirdObservationList(birdResults)

        GoogleMapsView { latLng ->
            selectedLocation = latLng
        }

        Text(
            text = "This is the Search By $searchBy tab inside the Search screen!",
            style = CustomTypography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxSize()
        )

        if (selectedLocation != null) {
            CallBirdAPI(selectedLocation!!, viewModel)
        }
    }
}

private fun onKeyEvent(
    event: KeyEvent,
    locationQuery: TextFieldValue,
    viewModel: FloraFaunaViewModel
): Boolean {
    if (event.key == Key.Enter && locationQuery.text.isNotBlank()) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                viewModel.searchBirds(locationQuery.text)
            } catch (e: Exception) {
                println("API call failed: ${e.message}")
            }
        }
        return true
    }
    return false
}


