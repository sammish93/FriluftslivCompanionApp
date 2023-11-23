package no.hiof.friluftslivcompanionapp.ui.screens

import android.Manifest
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.TripsState
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.ErrorView
import no.hiof.friluftslivcompanionapp.ui.components.SnackbarWithCondition
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapCreate
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.time.Duration

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TripsCreateScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel
) {

    // Variables for Bottom Sheet - see https://m3.material.io/components/bottom-sheets/overview
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val tripState by viewModel.tripsState.collectAsState()
    val userState by userViewModel.state.collectAsState()

    val locPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
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

    Box() {
        when (tripState.isLoading) {
            true -> CustomLoadingScreen()
            else -> if (!locPermissionState.status.isGranted) {


                Scaffold(
                    // A button that allows the user to click and display the Bottom Sheet.
                    floatingActionButton = {
                        if (isNetworkAvailable()) {
                            ExtendedFloatingActionButton(
                                text = { Text(stringResource(id = R.string.trips_create_create_trip)) },
                                icon = {
                                    Icon(
                                        Icons.Filled.Add,
                                        contentDescription = stringResource(id = R.string.trips_create_create_trip)
                                    )
                                },
                                onClick = {
                                    showBottomSheet = true
                                }
                            )
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { contentPadding ->
                    GoogleMapCreate(userViewModel, viewModel, modifier.padding(contentPadding))
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )

                    SnackbarWithCondition(
                        snackbarHostState = snackbarHostState,
                        message = (stringResource(R.string.noGpsMsg_CreateTripScreen)),
                        actionLabel = stringResource(R.string.understood),
                        condition = !locPermissionState.status.isGranted
                    )
                }
            } else if (tripState.isFailure) {
                ErrorView(message = stringResource(R.string.error_retrieving_api_success_response))


            } else {
                Scaffold(
                    // A button that allows the user to click and display the Bottom Sheet.
                    floatingActionButton = {
                        if (isNetworkAvailable()) {
                            ExtendedFloatingActionButton(
                                text = { Text(stringResource(id = R.string.trips_create_create_trip)) },
                                icon = {
                                    Icon(
                                        Icons.Filled.Add,
                                        contentDescription = stringResource(id = R.string.trips_create_create_trip)
                                    )
                                },
                                onClick = {
                                    showBottomSheet = true

                                    if (!isNetworkAvailable()) {
                                        showBottomSheet = false
                                        Toast.makeText(
                                            context,
                                            "No internet connection",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                }
                            )
                        }
                    },
                    floatingActionButtonPosition = FabPosition.Center
                ) { contentPadding ->
                    GoogleMapCreate(userViewModel, viewModel, modifier.padding(contentPadding))
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            TripsCreateSheet(tripState, viewModel, userState.language)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TripsCreateSheet(
    tripState: TripsState,
    viewModel: TripsViewModel,
    language: SupportedLanguage = SupportedLanguage.ENGLISH
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var dropdownSelectedText by remember { mutableIntStateOf(R.string.trips_create_dropdown_choose_something_exciting) }
    var isSelectedTripTypeEnabled by remember { mutableStateOf(true) }
    var showClearPopup by remember { mutableStateOf(false) }
    var showCreatePopup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .selectableGroup()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoButtonWithPopup()

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        // Type of trip.
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded, onExpandedChange = {
                dropdownExpanded = !dropdownExpanded
            }
        ) {

            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = stringResource(dropdownSelectedText),
                onValueChange = {},
                label = { Text(stringResource(R.string.trips_create_trip_type)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
            )

            // ExposedDropdownMenu is currently buggy - doesn't scale to width.
            DropdownMenu(
                expanded = dropdownExpanded, onDismissRequest = {
                    dropdownExpanded = false
                }, modifier = Modifier
                    .exposedDropdownSize()
            ) {
                viewModel.tripTypes.forEach { tripType ->
                    DropdownMenuItem(
                        text = { Text(text = stringResource(tripType.label)) },
                        onClick = {
                            isSelectedTripTypeEnabled = tripType.isEnabled
                            viewModel.updateCreateTripType(tripType)
                            dropdownSelectedText = tripType.label
                            dropdownExpanded = false
                        })
                }
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 12.dp))

        when (isSelectedTripTypeEnabled) {
            true -> {
                // Description Field
                TextField(
                    value = tripState.createTripDescription,
                    onValueChange = { viewModel.updateCreateTripDescription(it) },
                    label = { Text(stringResource(R.string.trips_create_description)) },
                    singleLine = false,
                    isError = tripState.createTripDescription.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                // Duration of trip in hours and minutes.
                Text(
                    text = (DateFormatter.formatDurationToPrettyString(
                        tripState.createTripDuration,
                        stringResource(R.string.hours),
                        stringResource(R.string.minutes)
                    )),
                    style = CustomTypography.headlineSmall
                )

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.hours),
                            textAlign = TextAlign.Center,
                            style = CustomTypography.titleMedium
                        )

                        // Buttons to increment and decrement trip duration's hour.
                        Row {
                            ElevatedButton(onClick = {
                                viewModel.updateCreateTripDuration(
                                    tripState.createTripDuration.minus(
                                        Duration.ofHours(1)
                                    )
                                )
                            }) {
                                Text(
                                    text = stringResource(R.string.symbol_minus),
                                    style = CustomTypography.headlineMedium
                                )
                            }

                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                            ElevatedButton(onClick = {
                                viewModel.updateCreateTripDuration(
                                    tripState.createTripDuration.plus(
                                        Duration.ofHours(1)
                                    )
                                )
                            }) {
                                Text(
                                    text = stringResource(R.string.symbol_plus),
                                    style = CustomTypography.headlineMedium
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(R.string.minutes),
                            textAlign = TextAlign.Center,
                            style = CustomTypography.titleMedium
                        )

                        // Buttons to increment and decrement trip duration's minutes.
                        Row {
                            ElevatedButton(onClick = {
                                viewModel.updateCreateTripDuration(
                                    tripState.createTripDuration.minus(
                                        Duration.ofMinutes(1)
                                    )
                                )
                            }) {
                                Text(
                                    text = stringResource(R.string.symbol_minus),
                                    style = CustomTypography.headlineMedium
                                )
                            }

                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                            ElevatedButton(onClick = {
                                viewModel.updateCreateTripDuration(
                                    tripState.createTripDuration.plus(
                                        Duration.ofMinutes(1)
                                    )
                                )
                            }) {
                                Text(
                                    text = stringResource(R.string.symbol_plus),
                                    style = CustomTypography.headlineMedium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                // Difficulty of trip from 1 to 5.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ElevatedButton(onClick = { viewModel.updateCreateTripDifficulty(tripState.createTripDifficulty - 1) }) {
                        Text(
                            text = stringResource(R.string.symbol_minus),
                            style = CustomTypography.headlineMedium
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Text(
                        TripFactory.convertTripDifficultyFromIntToString(
                            tripState.createTripDifficulty,
                            language
                        ),
                        style = CustomTypography.headlineSmall
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    ElevatedButton(onClick = { viewModel.updateCreateTripDifficulty(tripState.createTripDifficulty + 1) }) {
                        Text(
                            text = stringResource(R.string.symbol_plus),
                            style = CustomTypography.headlineMedium
                        )
                    }
                }
            }

            else -> {
                Text(stringResource(R.string.trips_create_dropdown_this_type_of_activity))
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

        // Create trip button.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showCreatePopup = true },
                enabled = isSelectedTripTypeEnabled,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                )
                Text(
                    stringResource(R.string.trips_create_create_trip),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

            Button(
                onClick = {
                    showClearPopup = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.trips_create_clear),
                )
                Text(
                    stringResource(R.string.trips_create_clear_trip),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }

    if (showCreatePopup) {
        val context = LocalContext.current

        // Alerts the user to confirm that they want to create a trip.
        AlertDialog(
            onDismissRequest = {
                showCreatePopup = false
                viewModel.createTrip()
            },
            title = { Text(text = stringResource(R.string.trips_create_dropdown_create_trip)) },
            text = { Text(stringResource(R.string.trips_create_dropdown_do_you_wish_create)) },
            confirmButton = {
                Button(onClick = {
                    showCreatePopup = false
                    viewModel.createTrip()
                    dropdownSelectedText = R.string.trips_create_dropdown_choose_something_exciting
                    Toast.makeText(context,
                        R.string.the_trip_is_now_available_and_displayed_on_the_map,
                        Toast.LENGTH_LONG).show()

                    viewModel.clearTrip()

                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showCreatePopup = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    // Alerts the user that they want to clear all trip related information.
    if (showClearPopup) {
        AlertDialog(
            onDismissRequest = { showClearPopup = false },
            title = { Text(text = stringResource(R.string.trips_create_dropdown_clear_trip)) },
            text = { Text(stringResource(R.string.trips_create_dropdown_do_you_wish_to_clear)) },
            confirmButton = {
                Button(onClick = {
                    showClearPopup = false
                    dropdownSelectedText = R.string.trips_create_dropdown_choose_something_exciting
                    viewModel.clearTrip()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showClearPopup = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}

// Popup that explains how the user can create a trip on the map.
@Composable
fun InfoButtonWithPopup() {
    var showPopup by remember { mutableStateOf(false) }

    val customShape: Shape = RoundedCornerShape(8.dp)

    // Info button
    Button(
        onClick = { showPopup = true },
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.trips_create_info),
        )
        Text(
            stringResource(R.string.trips_create_how_to_use_the_map),
            modifier = Modifier.padding(start = 4.dp)
        )
    }


    // Popup dialog.
    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text(text = stringResource(R.string.trips_create_how_to_use_the_map)) },
            text = { CardPopup() },
            confirmButton = {
                Button(onClick = { showPopup = false }) {
                    Text(stringResource(R.string.trips_create_got_it))
                }
            }
        )
    }
}

// Used with the InfoButtonWithPopup above - this is the actual popup contents.
@Composable
fun CardPopup() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.trips_create_tap_the_map),
                fontSize = 16.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.trips_create_hold_on_a_point),
                fontSize = 16.sp
            )
        }
    }
}
