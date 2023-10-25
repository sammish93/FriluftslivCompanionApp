package no.hiof.friluftslivcompanionapp.ui.screens

import android.location.Geocoder
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.models.DummyTrip
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapTripAdditionalInfo
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.time.LocalDate
import java.util.Locale

@Composable
fun TripsAdditionalInfoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    tripsViewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel,
) {
    val tripsState by tripsViewModel.tripsState.collectAsState()
    val userState by userViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBar(title = "Back To Trips", onBackClick = { navController.popBackStack() })
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
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
                            //TODO The real Trips is Duration - use DateFormatter class to make it
                            // pretty.
                            Text(tripsState.selectedTrip!!.duration)

                            Spacer(modifier = Modifier.padding(vertical = 4.dp))

                            Text(stringResource(R.string.distance))
                            Text(tripsState.selectedTrip!!.distance)

                            Spacer(modifier = Modifier.padding(vertical = 4.dp))

                            Text(stringResource(R.string.difficulty))
                            Text(TripFactory.convertTripDifficultyFromIntToString(tripsState.selectedTrip!!.difficulty))

                            Spacer(modifier = Modifier.padding(vertical = 4.dp))

                            Text(stringResource(R.string.trips_create_description))
                            Text(tripsState.selectedTrip!!.description)
                        }

                        //TODO add if (!tripsState.isSelectedTripRecentActivity) then show this.
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = { //TODO Do this.
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.trips_add_to_recent_activity),
                                )
                                Text(
                                    stringResource(R.string.trips_recent_activity_add_trip),
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

                            GoogleMapTripAdditionalInfo(nodes = tripsState.selectedTrip!!.nodes)
                        }
                    }
                }
            }
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
            text = "Error retrieving trip information.",
            style = CustomTypography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}

