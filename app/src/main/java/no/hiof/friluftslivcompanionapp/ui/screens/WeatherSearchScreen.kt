package no.hiof.friluftslivcompanionapp.ui.screens

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.WeatherState
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.LocationAutoFillList
import no.hiof.friluftslivcompanionapp.ui.components.SnackbarWithCondition
import no.hiof.friluftslivcompanionapp.ui.components.cards.PrimaryWeatherCard
import no.hiof.friluftslivcompanionapp.ui.components.cards.SecondaryWeatherCard
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
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
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    // Code inspired by ChatGPT V3.5
    // Retrieves a boolean value as to whether the user currently has internet connectivity.
    val connectivityManager = remember { context.getSystemService(ConnectivityManager::class.java) }
    val isNetworkAvailable by rememberUpdatedState {
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    Log.d("NetworkStatus", "NetworkAvailable: $isNetworkAvailable, isNotNetworkAvailable: ${!isNetworkAvailable()}")

    // This resets the focus when a location has been selected from the Places API.
    val focusedElement = LocalFocusManager.current

    // Shows the Places API - the search bar.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
                if(!isNetworkAvailable()){
                    Text(
                        text = stringResource(R.string.enable_network_to_search_for_a_place),
                        style = CustomTypography.labelLarge
                    )

                } else{
                    Text(
                        text = stringResource(R.string.search_search_for_a_place),
                        style = CustomTypography.labelLarge
                    )}
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(),
            leadingIcon = {
                if (!isNetworkAvailable()){
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(R.string.warningicon)
                    )

                } else{
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon)
                    )
                }
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


        // Shows the results of the search dynamically updating.
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

            else ->{
                //if(isNotNetworkAvailable){}
                Box(){
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )

                    SnackbarWithCondition(
                        snackbarHostState = snackbarHostState,
                        message = stringResource(R.string.no_internett_connection),
                        actionLabel = stringResource(R.string.understood),
                        condition = !isNetworkAvailable()
                    )
                }
            }
        }

        // Shows the weather forecast for the location chosen.
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
                                when (userState.windowSizeClass.widthSizeClass) {
                                    // Layout of the cards when screen width is compact.
                                    WindowWidthSizeClass.Compact -> {
                                        ForecastCardsForSearch(
                                            weatherState,
                                            userState.language,
                                            false
                                        )
                                    }

                                    // Layout of the cards for everything else.
                                    else -> {
                                        ForecastCardsForSearch(
                                            weatherState,
                                            userState.language,
                                            true
                                        )
                                    }
                                }
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
    language: SupportedLanguage,
    isWide: Boolean
) {

    when (isWide) {
        // The whole list is scrollable when screen width is wide.
        true -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState(), enabled = isWide),
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Row {
                    weatherState.currentWeather?.let {
                        PrimaryWeatherCard(
                            weather = it,
                            units = weatherState.unitChoice,
                            language = language,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    weatherState.todayWeather?.let {
                        PrimaryWeatherCard(
                            weather = it,
                            units = weatherState.unitChoice,
                            current = false,
                            language = language,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // This column is scrollable.
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    SecondaryCards(weatherState, language)
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                // This section before the next column remains in position while the rest
                // of the list is scrollable.
                Spacer(modifier = Modifier.height(20.dp))

                weatherState.currentWeather?.let {
                    PrimaryWeatherCard(
                        weather = it,
                        units = weatherState.unitChoice,
                        language = language
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                weatherState.todayWeather?.let {
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
                    SecondaryCards(weatherState, language)
                }
            }
        }
    }
}

@Composable
private fun SecondaryCards(
    weatherState: WeatherState,
    language: SupportedLanguage
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










