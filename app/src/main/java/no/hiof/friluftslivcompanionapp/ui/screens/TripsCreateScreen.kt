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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
            //type of trip

            //duration
            Button(onClick = {
                viewModel.updateCreateTripDuration(
                    tripState.createTripDuration.minus(
                        Duration.ofHours(1)
                    )
                )
            }) {
                Text(text = "- " + stringResource(R.string.hours))
            }
            Button(onClick = {
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
            Button(onClick = {
                viewModel.updateCreateTripDuration(
                    tripState.createTripDuration.plus(
                        Duration.ofMinutes(1)
                    )
                )
            }) {
                Text(text = "+ " + stringResource(R.string.minutes))
            }
            Button(onClick = {
                viewModel.updateCreateTripDuration(
                    tripState.createTripDuration.plus(
                        Duration.ofHours(1)
                    )
                )
            }) {
                Text(text = "+ " + stringResource(R.string.hours))
            }

            //difficulty
            Button(onClick = { viewModel.updateCreateTripDifficulty(tripState.createTripDifficulty - 1) }) {
                Text(text = "-")
            }
            Text(tripState.createTripDifficulty.toString())
            Button(onClick = { viewModel.updateCreateTripDifficulty(tripState.createTripDifficulty + 1) }) {
                Text(text = "+")
            }

            // Description Field
            TextField(
                value = tripState.createTripDescription,
                onValueChange = { viewModel.updateCreateTripDescription(it) },
                label = { Text("Description") },
                singleLine = false
            )

            // Create trip button.
            Button(
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
                Text("Create Trip", modifier = Modifier.padding(start = 4.dp))
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
        Text("How to use the map", modifier = Modifier.padding(start = 4.dp))
    }


    // Popup dialog.
    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text(text = "How to use the map") },
            text = { CardPopup() },
            confirmButton = {
                Button(onClick = { showPopup = false }) {
                    Text("Got it!")
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
                text = "Tap the map to add points and draw routes.",
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
                text = "Hold on a point to remove it from the route.",
                fontSize = 16.sp
            )
        }
    }
}