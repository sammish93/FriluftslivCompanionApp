package no.hiof.friluftslivcompanionapp.ui.screens

import android.location.Geocoder
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.domain.LocationFormatter
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.ListComponent
import no.hiof.friluftslivcompanionapp.ui.components.LocationAutoFillList
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
    val floraFaunaState by viewModel.floraFaunaState.collectAsState()
    val placesState by userViewModel.placeInfoState.collectAsState()

    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val locations = geocoder.getFromLocation(
        userState.lastKnownLocation?.latitude ?: DefaultLocation.OSLO.lat,
        userState.lastKnownLocation?.longitude ?: DefaultLocation.OSLO.lon,
        1
    )

    var text by remember { mutableStateOf("") }
    var birdResultsShown by remember { mutableStateOf(false) }
    var resultListShown by remember { mutableStateOf(false) }
    val focusedElement = LocalFocusManager.current

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
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    userViewModel.searchPlaces(it, SupportedLanguage.NORWEGIAN.code)
                    birdResultsShown = false
                    resultListShown = true
                },
                label = {
                    Text(
                        text = stringResource(R.string.search_search_for_a_place),
                        style = TextStyle(fontWeight = FontWeight.Medium)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = TextFieldDefaults.colors(),
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.search_clear_text),
                        modifier = Modifier.clickable {
                            text = ""
                            userViewModel.clearAutocompleteResults()
                        }
                    )
                }
            )
        }

        when (resultListShown) {
            true -> {
                LocationAutoFillList(
                    viewModel = userViewModel,
                    onAddressSelected = { selectedAddress ->
                        text = selectedAddress
                        resultListShown = false
                        focusedElement.clearFocus()
                    }
                )
            }

            else -> {
                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = {
                            if (!locations.isNullOrEmpty()) {
                                val location = locations[0]

                                val locality = location.adminArea ?: "Oslo"

                                viewModel.viewModelScope.launch {
                                    val (regionCode, message) = LocationFormatter.getRegionCodeByLocation(
                                        locality
                                    )
                                    println("Found your location: $regionCode")
                                    println(message)
                                    viewModel.searchBirdsByLocation(
                                        regionCode,
                                        20,
                                        userState.language
                                    )
                                }
                            } else {
                                println("Unable to get location. Using default location: Oslo")
                                viewModel.viewModelScope.launch {
                                    val (regionCode, message) = LocationFormatter.getRegionCodeByLocation(
                                        "Oslo"
                                    )
                                    println(message)
                                    viewModel.searchBirdsByLocation(
                                        regionCode,
                                        20,
                                        userState.language
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .height(40.dp),
                        enabled = if (!locations.isNullOrEmpty()) {
                            locations[0].countryCode == "NO"
                        } else false
                    ) {
                        Text(text = "Use my location")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val locality = placesState?.county ?: "Oslo"

                            viewModel.viewModelScope.launch {
                                val (regionCode, message) = LocationFormatter.getRegionCodeByLocation(
                                    locality
                                )
                                println("Found your location: $regionCode")
                                println(message)
                                viewModel.searchBirdsByLocation(regionCode, 20, userState.language)
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .height(40.dp),
                        enabled = text.isNotEmpty()

                    ) {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = "Search",
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)

                        )
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            when (floraFaunaState.isLoading) {
                true -> CustomLoadingScreen()

                else -> {
                    ListComponent(floraFaunaState.birdResults) { bird, textStyle ->
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
}