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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaAdditionalInfo (
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
                    ExtendedFloatingActionButton(
                        text = { Text("Add Sighting") },
                        icon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Add Sighting"
                            )
                        },
                        onClick = {
                            showBottomSheet = true
                        }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            TopBar(title = "Back to results", onBackClick = { navController.popBackStack() })
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
                    var species = floraFaunaState.selectedSpecies
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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

                        Image(
                            painter = rememberImagePainter(data = species?.photoUrl),
                            contentDescription = "Photo of ${species?.speciesName}",
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
            SpeciesBottomSheet(floraFaunaState, viewModel, userViewModel)
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
            text = "Error retrieving species information",
            style = CustomTypography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}

@Composable
fun SpeciesBottomSheet(
    floraFaunaState: FloraFaunaState,
    floraFaunaViewModel: FloraFaunaViewModel,
    userViewModel: UserViewModel
) {
    val userState by userViewModel.state.collectAsState()
    var showAddPopup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .selectableGroup()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //TODO Add date picker.

        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

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
                    "Add to lifelist",
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }

    if (showAddPopup) {
        AlertDialog(
            onDismissRequest = {
                showAddPopup = false
            },
            title = { Text(text = "Add to lifelist?") },
            text = { Text("Do you wish to add this sighting to your lifelist?") },
            confirmButton = {
                Button(onClick = {
                    showAddPopup = false
                    floraFaunaViewModel.updateSightingLocation(Location(userState.lastKnownLocation!!.latitude, userState.lastKnownLocation!!.longitude))
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