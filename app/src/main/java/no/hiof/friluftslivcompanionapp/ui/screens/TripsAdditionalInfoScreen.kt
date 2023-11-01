package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.FloraFaunaState
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapTripAdditionalInfo
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsAdditionalInfoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    tripsViewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel,
) {
    val tripsState by tripsViewModel.tripsState.collectAsState()
    val userState by userViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.navigation_back_to_trips),
                onBackClick = { navController.popBackStack() })
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (tripsState.selectedTrip) {
                null -> NoTripFoundScreen()

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxSize()
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top
                        ) {

                            //TODO Use DateFormatter and modify a function to include year.
                            // also add if (tripsState.isSelectedTripRecentActivity) then show date.
                            Text(stringResource(R.string.date))
                            Text(LocalDate.now().toString())

                            Text(stringResource(R.string.duration))
                            tripsState.selectedTrip?.duration?.let {
                                DateFormatter.formatDurationToPrettyString(it, "Hour", "Minute")
                            }?.let { formattedDuration ->
                                Text(text = formattedDuration)
                            }

                            Spacer(modifier = Modifier.padding(vertical = 4.dp))

                            Text(stringResource(R.string.distance))
                            Text(tripsState.selectedTrip!!.distanceKm.toString())

                            Spacer(modifier = Modifier.padding(vertical = 4.dp))

                            Text(stringResource(R.string.difficulty))
                            tripsState.selectedTrip?.difficulty?.let {
                                TripFactory.convertTripDifficultyFromIntToString(it)
                            }?.let { formattedDifficulty ->
                                Text(text = formattedDifficulty)
                            }

                            Spacer(modifier = Modifier.padding(vertical = 4.dp))

                            Text(stringResource(R.string.trips_create_description))
                            tripsState.selectedTrip!!.description?.let { it ->
                                Text(it.replaceFirstChar { it.uppercase() })
                            }
                        }

                        //TODO add if (!tripsState.isSelectedTripRecentActivity) then show this.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    showBottomSheet = true
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add),
                                )
                                Text(
                                    stringResource(R.string.trips_add_to_trip_log),
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                            //TODO Add date picker here.
                        }

                        Column(
                            modifier = Modifier
                                .weight(2f)
                                .clip(RoundedCornerShape(20.dp)),
                            verticalArrangement = Arrangement.Bottom
                        ) {

                            GoogleMapTripAdditionalInfo(nodes = tripsState.selectedTrip!!.route)
                        }
                    }
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
            TripsBottomSheet(tripsViewModel, userViewModel)
        }
    }
}

@Composable
fun NoTripFoundScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.trip_additional_info_error_retrieving_trip_information),
            style = CustomTypography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsBottomSheet(
    tripsViewModel: TripsViewModel,
    userViewModel: UserViewModel
) {
    val userState by userViewModel.state.collectAsState()
    var showAddPopup by remember { mutableStateOf(false) }
    var showDatePickerPopup by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
        initialDisplayMode = DisplayMode.Picker,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        }
    )

    val df = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    Column(
        modifier = Modifier
            .selectableGroup()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = df.format(datePickerState.selectedDateMillis?.let { Date(it) }!!),
            onValueChange = {
            },
            readOnly = true,
            label = { Text(text = stringResource(R.string.trips_date_of_trip)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Button(
            onClick = { showDatePickerPopup = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(id = R.string.date),
            )
            Text(
                stringResource(R.string.trips_choose_a_date),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    showAddPopup = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add),
                )
                Text(
                    stringResource(id = R.string.trips_add_to_trip_log)
                )
            }
        }
    }

    if (showDatePickerPopup) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = { showDatePickerPopup = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { Date(it) }?.let {
                        tripsViewModel.updateTripActivityDate(
                            it
                        )
                    }

                    showDatePickerPopup = false
                }

                ) {
                    Text(text = stringResource(R.string.okay))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis = Instant.now().toEpochMilli()
                        showDatePickerPopup = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(R.string.dismiss))
                }
            }
        ) {
            DatePicker(
                modifier = Modifier.padding(4.dp),
                state = datePickerState,
                title = {
                    Text(
                        text = stringResource(id = R.string.trips_choose_a_date),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                showModeToggle = false
            )
        }
    }

    if (showAddPopup) {
        AlertDialog(
            onDismissRequest = {
                showAddPopup = false
            },
            title = { Text(text = stringResource(R.string.trips_add_to_trip_log_question)) },
            text = { Text(stringResource(R.string.trips_do_you_wish_to_add)) },
            confirmButton = {
                Button(onClick = {
                    showAddPopup = false
                    tripsViewModel.createTripActivity()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showAddPopup = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}

