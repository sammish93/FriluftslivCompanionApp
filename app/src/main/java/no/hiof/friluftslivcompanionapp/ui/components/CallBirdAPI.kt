package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.LatLng
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel

@Composable
fun CallBirdAPI(selectedLocation: LatLng, viewModel: FloraFaunaViewModel) {
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

}