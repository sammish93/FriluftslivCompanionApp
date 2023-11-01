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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.components.items.Item
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapTripAdditionalInfo
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

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
            TopBar(title = stringResource(R.string.navigation_back_to_trips), onBackClick = { navController.popBackStack() })
        },
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
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
                            // Inspired from: https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#ListItem(kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Function0,kotlin.Function0,kotlin.Function0,kotlin.Function0,androidx.compose.material3.ListItemColors,androidx.compose.ui.unit.Dp,androidx.compose.ui.unit.Dp)
                            tripsState.selectedTrip?.duration?.let {
                                DateFormatter.formatDurationToPrettyString(it, "Hour", "Minute")
                            }?.let { formattedDuration ->
                                Item(
                                    headline = stringResource(R.string.duration),
                                    support = formattedDuration,
                                    icon = painterResource(id = R.drawable.timer),
                                    iconDescription = "Duration"
                                )
                            }
                            val roundedDistance = tripsState.selectedTrip!!.distanceKm?.let {
                                kotlin.math.round(it * 10) / 10
                            }
                            Item(
                                headline = stringResource(R.string.distance),
                                support = "${roundedDistance.toString()} km",
                                icon = painterResource(id = R.drawable.distance),
                                iconDescription = "Distance"
                            )
                            tripsState.selectedTrip?.difficulty?.let {
                                TripFactory.convertTripDifficultyFromIntToString(it)
                            }?.let { formattedDifficulty ->
                                Item(
                                    headline = stringResource(R.string.difficulty),
                                    support = formattedDifficulty,
                                    icon = painterResource(id = R.drawable.lock),
                                    iconDescription = "Difficulty"
                                )
                            }
                            tripsState.selectedTrip!!.description?.let { description ->
                                Item(
                                    headline = stringResource(R.string.trips_create_description),
                                    support = description.replaceFirstChar { it.uppercase() },
                                    icon = painterResource(id = R.drawable.description),
                                    iconDescription = "Description"
                                )
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

                            GoogleMapTripAdditionalInfo(nodes = tripsState.selectedTrip!!.route)
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
            text = stringResource(R.string.trip_additional_info_error_retrieving_trip_information),
            style = CustomTypography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )
    }
}

