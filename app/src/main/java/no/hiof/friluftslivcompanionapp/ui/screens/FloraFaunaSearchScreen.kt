package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.maps.android.compose.*
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.ui.components.ListComponent
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapsView
import java.time.LocalDateTime
import no.hiof.friluftslivcompanionapp.data.network.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaSearchScreen(
    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel()
) {
    //val dummyBirds = generateDummyBirds()
    var birds by remember {
        mutableStateOf(emptyList<Bird>())
    }

    //  var locationName by remember {
    //        mutableStateOf("")
    
    Box {
        LaunchedEffect(Unit) {
            val api = BirdObservations.getInstance()
            val defaultRegionCode = "NO-03" // Default region code for Oslo

            // Call the suspend function from within a coroutine
            val result = coroutineScope {
                async {
                    api.getRecentObservations(regionCode = defaultRegionCode, maxResult = 3)
                }.await()
            }

            when (result) {
                is Result.Success -> {
                    birds = result.value // Update the birds when the API call is successful
                }
                is Result.Failure -> {
                    println("Error fetching bird observations: ${result.message}")
                }
            }
        }


    }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            birds.forEach { bird ->
                Text(
                    text = bird.speciesName ?: "",
                    style = MaterialTheme.typography.headlineLarge
                )
                // Display other bird information as needed
            }
        }
    }

    /*
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),

        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = locationName,
                onValueChange = { locationName = it },
                placeholder = { Text(text = "Enter your location to search") },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                singleLine = true
            )

        }
        Row ( modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween, ){
            Button(
                onClick = {
                    viewModel.setLocation(locationName)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search",
                    modifier= Modifier
                        .height(40.dp)
                        .width(40.dp))
            }

        }
         Row {
            ListComponent(items = dummyBirds) { bird, _ ->
                // Vis fugledata her
                Text(text = bird.speciesName ?: "", style = MaterialTheme.typography.headlineLarge)
            }
        }
            Row {
                ListComponent(items = birds) { bird, _ ->
                    // Vis fugledata her
                    Text(
                        text = bird.speciesName ?: "",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }

//GoogleMapsView(locationName, viewModel)

fun generateDummyBirds(): List<Bird> {
    val dummyBirds = mutableListOf<Bird>()

    // Legg til noen dummy fugledata
    dummyBirds.add(
        Bird(
            speciesName = "Sparrow",
            speciesNameScientific = "Passer domesticus",
            number = 10,
            description = "A small bird",
            photoUrl = "https://example.com/sparrow.jpg",
            observationDate = LocalDateTime.now(),
            coordinates = Location(12.345, 67.890)
        )
    )
    dummyBirds.add(
        Bird(
            speciesName = "Sparrow",
            speciesNameScientific = "Passer domesticus",
            number = 10,
            description = "A small bird",
            photoUrl = "https://example.com/sparrow.jpg",
            observationDate = LocalDateTime.now(),
            coordinates = Location(12.345, 67.890)
        )
    )
    dummyBirds.add(
        Bird(
            speciesName = "Sparrow",
            speciesNameScientific = "Passer domesticus",
            number = 10,
            description = "A small bird",
            photoUrl = "https://example.com/sparrow.jpg",
            observationDate = LocalDateTime.now(),
            coordinates = Location(12.345, 67.890)
        )
    )

    return dummyBirds
}*/