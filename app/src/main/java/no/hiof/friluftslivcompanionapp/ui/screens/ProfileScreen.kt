package no.hiof.friluftslivcompanionapp.ui.screens


import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.SignInActivity
import no.hiof.friluftslivcompanionapp.domain.NameFormatter
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.DisplayPicture
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.SettingsBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val userState by userViewModel.state.collectAsState()
    val tripCount by userViewModel.tripCountForTheYear.collectAsState()

    val totalDistance by userViewModel.totalKilometers.collectAsState()
    val totalSpeciesCount by userViewModel.speciesCount.collectAsState()

    val topThreeTrips by userViewModel.topThreeUsersByTripCount.collectAsState()
    val topThreeSpecies by userViewModel.topThreeUsersBySpeciesCount.collectAsState()

    val context = LocalContext.current
    val logoutState by userViewModel.logoutState.collectAsState()

    if (logoutState) {

        val intent = Intent(context, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }


    // Queries the database for a user's stats for the current year.
    LaunchedEffect(true) {
        userViewModel.fetchTripCountForTheYear()
        userViewModel.fetchTotalKilometersForTheYear()
        userViewModel.fetchSpeciesCountForThisYear()
        userViewModel.fetchTopThreeUsersByTripCount()
        userViewModel.fetchTopThreeUsersBySpeciesCount()
    }

    Scaffold(
        topBar = {
            SettingsBar(
                onClick = { navController.navigate(Screen.PROFILE_SETTINGS.name) },
                userViewModel = userViewModel
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Section of the page that shows a user's stats for the year.
                Text(text = stringResource(R.string.profile_your_year_so_far, LocalDate.now().year),
                    style = CustomTypography.headlineSmall)

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.tertiaryContainer,
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                    ) {
                        //TODO Implement functionality to calculate amount of recentactivity and
                        // lifelist.

                        // Number of trails a user has added to their trip log.
                        Text(text = stringResource(R.string.profile_number_trails_completed, "$tripCount"))

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        // Number of kilometers travelled (trips total distance)
                        Text(
                            text = stringResource(
                                R.string.profile_number_kilometers_travelled,
                                "$totalDistance"
                            )
                        )

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        // Number of species added to their lifelist.
                        Text(
                            text = stringResource(
                                R.string.profile_number_individual_species_identified,
                                "$totalSpeciesCount"
                            )
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Section to display the top species spotters and trip-goers.
                Text(text = stringResource(R.string.profile_year_leaderboard, LocalDate.now().year),
                    style = CustomTypography.headlineSmall)

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                    ) {

                        //TODO Implement functionality to get top 3 people with most recentactivity.
                        Text(text = stringResource(R.string.profile_trips_taken),
                            style = CustomTypography.titleLarge)

                        topThreeTrips?.forEachIndexed { index, user ->
                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                            LeaderboardRow(

                                displayPicture = user.preferences.displayPicture,
                                username = user.username,
                                numberToDisplay = user.yearlyTripCount,
                                placement = index + 1,
                                padding = if (index == 0) 0 else if (index == 1) 4 else 8,
                                scale = if (index == 0) 1F else if (index == 1) 0.9F else 0.8F
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                        //TODO Implement functionality to get top 3 people with most sightings.
                        Text(text = stringResource(R.string.profile_species_spotted),
                                style = CustomTypography.titleLarge)
                        topThreeSpecies?.forEachIndexed { index, user ->
                            Spacer(modifier = Modifier.padding(vertical = 4.dp))
                            LeaderboardRow(

                                displayPicture = user.preferences.displayPicture,
                                username = user.username,
                                numberToDisplay = user.yearlySpeciesCount,
                                placement = index + 1,
                                padding = if (index == 0) 0 else if (index == 1) 4 else 8,
                                scale = if (index == 0) 1F else if (index == 1) 0.9F else 0.8F
                            )
                        }

                    }
                }
                /*
                Text(text = stringResource(R.string.profile_trips_taken))

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                LeaderboardRow(DisplayPicture.DP_DEFAULT, "Jim", 42, 1)

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                LeaderboardRow(DisplayPicture.DP_ONE, "Joris", 38, 2, 4, 0.9F)

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                LeaderboardRow(DisplayPicture.DP_TWO, "Jonas", 34, 3, 8, 0.8F)

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                Text(text = stringResource(R.string.profile_species_spotted))

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                LeaderboardRow(DisplayPicture.DP_THREE, "Paddy", 104, 1)

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                LeaderboardRow(DisplayPicture.DP_FOUR, "Feargal", 97, 2, 4, 0.9F)

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                LeaderboardRow(DisplayPicture.DP_FIVE, "Colm", 82, 3, 8, 0.8F)
            }
        }
                 */
                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Button which allows the user to log out.
                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        userViewModel.logout()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.log_out),
                    )
                    Text(
                        stringResource(R.string.log_out),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

// Composable which displays a user leaderboard. Padding and scale values are given in the above
// composable to create an effect which allows the leaderboard to cascade downwards with first place
// being largest, 2nd being smaller, 3rd being smallest.
@Composable
private fun LeaderboardRow(
    displayPicture: DisplayPicture,
    username: String,
    numberToDisplay: Int,
    placement: Int,
    padding: Int = 0,
    scale: Float = 1.0F
) {
    val formattedUsername = username.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }

    Row(
        modifier = Modifier
            .padding(start = padding.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .border(
                        4.dp,
                        if (placement == 1) {
                            Color(0xFFC9B037)
                        } else if (placement == 2) {
                            Color(0xFFB4B4B4)
                        } else {
                            Color(0xFFAD8A56)
                        },
                        CircleShape
                    )
            ) {
                Image(
                    painter = painterResource(displayPicture.defaultResolution),
                    contentDescription = stringResource(R.string.display_picture),
                    contentScale = ContentScale.Crop
                )
            }

            if (placement == 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
            if (placement == 2) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

            Text(
                text = if (formattedUsername == "DefaultUsername")
                    stringResource(NameFormatter.getRandomName())
                else formattedUsername,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(numberToDisplay.toString())
    }
}


