package no.hiof.friluftslivcompanionapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.WeatherState
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.LocationAutoFillList
import no.hiof.friluftslivcompanionapp.ui.components.cards.PrimaryWeatherCard
import no.hiof.friluftslivcompanionapp.ui.components.cards.SecondaryWeatherCard
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.WeatherViewModel


//TODO Add behaviour if no internet connection is available.
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WeatherSearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    userViewModel
    val placesState by userViewModel.placeInfoState.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val userState by userViewModel.state.collectAsState()

    var text by remember { mutableStateOf("") }
    var resultListShown by remember { mutableStateOf(false) }
    var weatherResultsShown by remember { mutableStateOf(false) }
    val hasApiBeenCalled = remember { mutableStateOf(false) }
    val focusedElement = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                userViewModel.searchPlaces(it)
                weatherResultsShown = false
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

        when (resultListShown) {
            true -> {
                LocationAutoFillList(
                    viewModel = userViewModel,
                    onAddressSelected = { selectedAddress ->
                        text = selectedAddress
                        hasApiBeenCalled.value = false
                        resultListShown = false
                        focusedElement.clearFocus()
                        weatherResultsShown = true
                    }
                )
            }

            else -> {}
        }

        when (weatherResultsShown) {
            true -> {
                when (weatherState.isLoading || userState.isLocationSearchUpdating) {
                    // When the coroutine handling an API request is still running async then a progress bar
                    // is shown.
                    true -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            CustomLoadingScreen()
                        }
                    }

                    else -> {
                        LaunchedEffect(hasApiBeenCalled.value) {
                            if (!hasApiBeenCalled.value) {
                                viewModel.viewModelScope.launch {
                                    viewModel.getWeatherForecast(
                                        placesState?.coordinates?.latitude,
                                        placesState?.coordinates?.longitude,
                                        true
                                    )
                                }
                                hasApiBeenCalled.value = true
                            }
                        }

                        when (weatherState.isFailure) {
                            // When a successful reponse has been returned then the following is displayed.
                            false -> {
                                // The main content of the screen is in this block.
                                ForecastCardsForSearch(weatherState, userState.language)
                            }

                            else -> {}
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun ForecastCardsForSearch(
    weatherState: WeatherState,
    language: SupportedLanguage
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // This section before the next column remains in position while the rest
        // of the list is scrollable.
        weatherState.currentWeatherForSearch?.let {
            PrimaryWeatherCard(
                weather = it,
                units = weatherState.unitChoice,
                language = language
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        weatherState.todayWeatherForSearch?.let {
            PrimaryWeatherCard(
                weather = it,
                units = weatherState.unitChoice,
                current = false,
                language = language
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // This column is scrollable.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            weatherState.todayPlusOneWeatherForSearch?.let {
                SecondaryWeatherCard(
                    weather = it,
                    units = weatherState.unitChoice,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            weatherState.todayPlusTwoWeatherForSearch?.let {
                SecondaryWeatherCard(
                    weather = it,
                    units = weatherState.unitChoice,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            weatherState.todayPlusThreeWeatherForSearch?.let {
                SecondaryWeatherCard(
                    weather = it,
                    units = weatherState.unitChoice,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            weatherState.todayPlusFourWeatherForSearch?.let {
                SecondaryWeatherCard(
                    weather = it,
                    units = weatherState.unitChoice,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            weatherState.todayPlusFiveWeatherForSearch?.let {
                SecondaryWeatherCard(
                    weather = it,
                    units = weatherState.unitChoice,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            weatherState.todayPlusSixWeatherForSearch?.let {
                SecondaryWeatherCard(
                    weather = it,
                    units = weatherState.unitChoice,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            weatherState.todayPlusSevenWeatherForSearch?.let {
                SecondaryWeatherCard(
                    weather = it,
                    units = weatherState.unitChoice,
                    language = language
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}









