package no.hiof.friluftslivcompanionapp.ui.screens

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.ListComponent
import no.hiof.friluftslivcompanionapp.ui.components.items.CardList
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.util.Locale


@Composable
fun FloraFaunaSearchScreen(
    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val userState by userViewModel.state.collectAsState()
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val locations = geocoder.getFromLocation(
        userState.lastKnownLocation?.latitude ?: DefaultLocation.OSLO.lat,
        userState.lastKnownLocation?.longitude ?: DefaultLocation.OSLO.lon,
        1
    )

    var locationName by remember { mutableStateOf("") }
    val birdResults by viewModel.birdResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = locationName,
                onValueChange = { locationName = it },
                placeholder = { Text(text = "Enter your region code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                singleLine = true
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = {
                    if (!locations.isNullOrEmpty()) {
                        val location = locations[0]
                        val locality = location.adminArea ?: "Oslo"

                        viewModel.viewModelScope.launch {
                            val (regionCode, message) = viewModel.searchBirdsByYourLocation(locality)
                            println("Found your location: $regionCode")
                            println(message)
                            viewModel.searchBirdsByLocation(regionCode)
                        }
                    } else {
                        println("Unable to get location. Using default location: Oslo")
                        viewModel.viewModelScope.launch {
                            val (regionCode, message) = viewModel.searchBirdsByYourLocation("Oslo")
                            println(message)
                            viewModel.searchBirdsByLocation(regionCode)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(text = "Use my location")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.searchBirdsByLocation(locationName)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(40.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search",
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)

                )
            }

        }
        
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLoading) {
                CustomLoadingScreen()
            } else {
                ListComponent(birdResults) { bird, textStyle ->
                    CardList(
                        bird,
                        textStyle,
                        displayText = { it.speciesName ?: "Unknown Bird" },
                        fetchImage = { it.photoUrl ?: "Photo of ${it.speciesName}" }
                    ) {
                        viewModel.updateSelectedBirdInfo(bird)
                        navController.navigate(Screen.FLORA_FAUNA_ADDITIONAL_INFO.route)
                    }
                }
            }
        }
    }
}


//GoogleMapsView(locationName, viewModel)

