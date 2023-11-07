package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.data.states.FloraFaunaState
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaAdditionalInfo(
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),
    navController: NavController
) {
    val floraFaunaState by viewModel.floraFaunaState.collectAsState()
    val userState by userViewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            when (userState.lastKnownLocation) {
                null -> {}

                else -> {
                    if (floraFaunaState.selectedSpecies != null) {
                        ExtendedFloatingActionButton(
                            text = { Text(stringResource(R.string.flora_fauna_info_add_sighting)) },
                            icon = {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = stringResource(id = R.string.flora_fauna_info_add_sighting)
                                )
                            },
                            onClick = {
                                showBottomSheet = true
                            }
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            TopBar(
                title = stringResource(R.string.flora_fauna_info_back_to_results),
                onBackClick = { navController.popBackStack() })
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (floraFaunaState.selectedSpecies) {
                null -> NoSpeciesFoundScreen()

                else -> {
                    // Displays information relating to the species selected.
                    var species = floraFaunaState.selectedSpecies
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (userState.windowSizeClass.widthSizeClass) {
                            // Layout of info when screen width is compact. Single column with
                            // scrollable description.
                            WindowWidthSizeClass.Compact -> {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 20.dp, end = 20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${species?.speciesName}",
                                        style = CustomTypography.headlineSmall
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "${species?.speciesNameScientific}",
                                        style = CustomTypography.bodyLarge,
                                        fontStyle = FontStyle.Italic
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Image spans the whole width of the screen.
                                Image(
                                    painter = rememberImagePainter(data = species?.photoUrl),
                                    contentDescription = "${species?.speciesName}",
                                    modifier = Modifier
                                        .height(180.dp)
                                        .fillMaxWidth(),
                                    //.clip(RoundedCornerShape(12.dp))
                                    //.wrapContentHeight(),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Column(
                                    modifier = Modifier
                                        .padding(start = 20.dp, end = 20.dp)
                                        .verticalScroll(rememberScrollState()),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${species?.description}",
                                        style = CustomTypography.bodyMedium
                                    )

                                    Spacer(modifier = Modifier.height(80.dp))
                                }
                            }

                            // Layout of info when screen width is wide. Two columns with right-most
                            // col including scrollable description. Left column includes species
                            // name and image.
                            else -> {
                                Column {
                                    Row {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(start = 4.dp),
                                                text = "${species?.speciesName}",
                                                style = CustomTypography.headlineSmall
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                modifier = Modifier.padding(start = 4.dp),
                                                text = "${species?.speciesNameScientific}",
                                                style = CustomTypography.bodyLarge,
                                                fontStyle = FontStyle.Italic
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Image(
                                                painter = rememberImagePainter(data = species?.photoUrl),
                                                contentDescription = "${species?.speciesName}",
                                                modifier = Modifier
                                                    .height(180.dp)
                                                    .fillMaxWidth(),
                                                //.clip(RoundedCornerShape(12.dp))
                                                //.wrapContentHeight(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Column(
                                            modifier = Modifier
                                                .padding(start = 20.dp, end = 20.dp)
                                                .verticalScroll(rememberScrollState())
                                                .weight(2f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "${species?.description}",
                                                style = CustomTypography.bodyMedium
                                            )

                                            Spacer(modifier = Modifier.height(80.dp))
                                        }
                                    }
                                }
                            }
                        }


                    }
                }
            }
        }
    }

    // Bottom sheet for adding species to lifelist.
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            SpeciesBottomSheet(viewModel, userViewModel)
        }
    }
}

@Composable
fun NoSpeciesFoundScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.flora_fauna_info_error_retrieving_species_information),
            style = CustomTypography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}

// TODO Add functionality to select a location if we have enough time.

// Bottom sheet where user can select a date they saw the bird. The user then adds the sighting to
// their lifelist using their current GPS location.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeciesBottomSheet(
    floraFaunaViewModel: FloraFaunaViewModel,
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
            label = { Text(text = stringResource(R.string.flora_fauna_info_date_of_sighting)) },
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
                stringResource(R.string.flora_fauna_info_choose_a_date),
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
                    stringResource(R.string.flora_fauna_info_add_to_lifelist)
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
                        floraFaunaViewModel.updateSightingDate(
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
                        text = stringResource(id = R.string.flora_fauna_info_choose_a_date),
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
            title = { Text(text = stringResource(R.string.flora_fauna_info_add_to_lifelist_question)) },
            text = { Text(stringResource(R.string.flora_fauna_info_do_you_wish_to_add)) },
            confirmButton = {
                Button(onClick = {
                    showAddPopup = false
                    floraFaunaViewModel.updateSightingLocation(
                        Location(
                            userState.lastKnownLocation!!.latitude,
                            userState.lastKnownLocation!!.longitude
                        )
                    )
                    floraFaunaViewModel.createSighting()
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