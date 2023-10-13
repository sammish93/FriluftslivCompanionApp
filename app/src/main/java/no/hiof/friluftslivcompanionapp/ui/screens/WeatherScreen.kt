package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel()
) {
    // On screen creation (first navigation) the following code is executed.
    LaunchedEffect(true) {
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.getWeatherForecast()
        }
    }

    // Collects a state that can be read from a composable.
    val weatherState by viewModel.weatherState.collectAsState()

    when (weatherState.isLoading) {
        // When the coroutine handling an API request is still running async then a progress bar
        // is shown.
        true -> CircularProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        )

        else -> when (weatherState.isFailure) {
            // When a successful reponse has been returned then the following is displayed.
            false -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = weatherState.currentWeather.toString(),
                        style = CustomTypography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                    // Button that launches a coroutine to send an API request to get an updated
                    // weather forecast.
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            viewModel.getWeatherForecast()
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            }

            // When a failure response has been returned then the following is displayed.
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize().wrapContentSize(Alignment.Center)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.error_retrieving_api_success_response),
                        style = CustomTypography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = modifier.wrapContentSize(Alignment.Center)
                    )
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            viewModel.getWeatherForecast()
                        }
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            }
        }
    }
}