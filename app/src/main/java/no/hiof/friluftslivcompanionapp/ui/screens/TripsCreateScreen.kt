package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapCreate
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
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
    var dropdownExpanded by remember { mutableStateOf(false) }
    var dropdownSelectedText by remember { mutableIntStateOf(R.string.trips_create_dropdown_choose_something_exciting) }

    val tripState by viewModel.tripsState.collectAsState()

    Scaffold(
        // A button that allows the user to click and display the Bottom Sheet.
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Create Trip") },
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Create Trip"
                    )
                },
                onClick = {
                    showBottomSheet = true
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { contentPadding ->
        GoogleMapCreate(userViewModel, viewModel, modifier.padding(contentPadding))
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Type of trip.
            ExposedDropdownMenuBox(expanded = dropdownExpanded, onExpandedChange = {
                dropdownExpanded = !dropdownExpanded
            }) {

                TextField(
                    modifier = Modifier
                        .menuAnchor(),
                    readOnly = true,
                    value = stringResource(dropdownSelectedText),
                    onValueChange = {},
                    label = { Text(stringResource(R.string.trips_create_trip_type)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                )

                ExposedDropdownMenu(expanded = dropdownExpanded, onDismissRequest = {
                    dropdownExpanded = false
                }) {
                    viewModel.tripTypes.forEach { tripType ->
                        DropdownMenuItem(
                            text = { Text(text = stringResource(tripType.label)) },
                            onClick = {
                                viewModel.updateCreateTripType(tripType)
                                dropdownSelectedText = tripType.label
                                dropdownExpanded = false
                            })
                    }
                }
            }

            // Duration of trip in hours and minutes.
            ElevatedButton(onClick = {
                viewModel.updateCreateTripDuration(
                    tripState.createTripDuration.minus(
                        Duration.ofHours(1)
                    )
                )
            }) {
                Text(text = "- " + stringResource(R.string.hours))
            }
            ElevatedButton(onClick = {
                viewModel.updateCreateTripDuration(
                    tripState.createTripDuration.minus(
                        Duration.ofMinutes(1)
                    )
                )
            }) {
                Text(text = "- " + stringResource(R.string.minutes))
            }
            Text(
                DateFormatter.formatDurationToPrettyString(
                    tripState.createTripDuration,
                    stringResource(R.string.hours),
                    stringResource(R.string.minutes)
                )
            )
            ElevatedButton(onClick = {
                viewModel.updateCreateTripDuration(
                    tripState.createTripDuration.plus(
                        Duration.ofMinutes(1)
                    )
                )
            }) {
                Text(text = "+ " + stringResource(R.string.minutes))
            }
            ElevatedButton(onClick = {
                viewModel.updateCreateTripDuration(
                    tripState.createTripDuration.plus(
                        Duration.ofHours(1)
                    )
                )
            }) {
                Text(text = "+ " + stringResource(R.string.hours))
            }

            // Difficulty of trip from 1 to 5.
            ElevatedButton(onClick = { viewModel.updateCreateTripDifficulty(tripState.createTripDifficulty - 1) }) {
                Text(text = "-")
            }
            Text(TripFactory.convertTripDifficultyFromIntToString(tripState.createTripDifficulty))
            ElevatedButton(onClick = { viewModel.updateCreateTripDifficulty(tripState.createTripDifficulty + 1) }) {
                Text(text = "+")
            }

            // Description Field
            TextField(
                value = tripState.createTripDescription,
                onValueChange = { viewModel.updateCreateTripDescription(it) },
                label = { Text(stringResource(R.string.trips_create_description)) },
                singleLine = false,
                isError = tripState.createTripDescription.isBlank()
            )

            // Create trip button.
            Button(
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
                Text(stringResource(R.string.trips_create_create_trip), modifier = Modifier.padding(start = 4.dp))
            }

            Button(
                onClick = {
                    dropdownSelectedText = R.string.trips_create_dropdown_choose_something_exciting
                    viewModel.clearTrip()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                )
                Text(stringResource(R.string.trips_create_clear_trip), modifier = Modifier.padding(start = 4.dp))
            }

            HorizontalDivider()

            InfoButtonWithPopup()
        }
    }
}

@Composable
fun InfoButtonWithPopup(modifier: Modifier = Modifier) {
    var showPopup by remember { mutableStateOf(false) }

    val customShape: Shape = RoundedCornerShape(8.dp)

    // Info button
    Button(
        onClick = { showPopup = true },
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
        )
        Text(stringResource(R.string.trips_create_how_to_use_the_map), modifier = Modifier.padding(start = 4.dp))
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