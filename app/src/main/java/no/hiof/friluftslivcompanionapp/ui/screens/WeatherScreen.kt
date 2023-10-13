package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.enums.WeatherUnit
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.components.WeatherCard
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

    // Variables for Bottom Sheet - see https://m3.material.io/components/bottom-sheets/overview
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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
                Scaffold(
                    // A button that allows the user to click and display the Bottom Sheet.
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = { Text("Settings") },
                            icon = {
                                Icon(
                                    Icons.Filled.Settings,
                                    contentDescription = "Change Forecast Settings"
                                )
                            },
                            onClick = {
                                showBottomSheet = true
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) { contentPadding ->

                    // The main content of the screen is in this block.
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                        verticalArrangement = Arrangement.Center
                    ) {
                        weatherState.currentWeather?.let {
                            WeatherCard(
                                weather = it,
                                units = weatherState.unitChoice
                            )
                        }
                    }
                }
            }

            // When a failure response has been returned then the following is displayed.
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
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

    // Bottom Sheet display and behaviour. Note that it's located here and not in the 'when' tests
    // because it causes a rerender of the rest of the page. If it was located nested in the 'when'
    // blocks then it would itself be rerendered.
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                Modifier.selectableGroup().padding(horizontal = 20.dp).padding(bottom = 24.dp)
            ) {
                viewModel.radioOptions.forEach { option ->
                    val unitEnum = option.key
                    val unitLabel = option.value

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .selectable(
                                selected = (unitEnum == weatherState.unitChoice),
                                onClick = {
                                    viewModel.updateWeatherUnit(unitEnum)
                                    CoroutineScope(Dispatchers.Default).launch {
                                        viewModel.getWeatherForecast()
                                    }
                                },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (unitEnum == weatherState.unitChoice),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = unitLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
                Button(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            viewModel.getWeatherForecast()
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = "Refresh"
                    )
                    Text("Update Weather Forecast", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }
    }
}