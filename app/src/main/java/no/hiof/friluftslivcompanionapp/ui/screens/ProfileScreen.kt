package no.hiof.friluftslivcompanionapp.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.type.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.DisplayPicture
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.SettingsBar
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
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



    LaunchedEffect(true) {
        userViewModel.fetchTripCountForTheYear()
        userViewModel.fetchTotalKilometersForTheYear()
        userViewModel.fetchSpeciesCountForThisYear()
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
                Text(text = stringResource(R.string.profile_your_year_so_far, LocalDate.now().year))

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
                        Text(text = stringResource(R.string.profile_number_trails_completed, "$tripCount"))

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        Text(
                            text = stringResource(
                                R.string.profile_number_kilometers_travelled,
                                "$totalDistance"
                            )
                        )

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        Text(
                            text = stringResource(
                                R.string.profile_number_individual_species_identified,
                                "$totalSpeciesCount"
                            )
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                Text(text = stringResource(R.string.profile_year_leaderboard, LocalDate.now().year))

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
                        Text(text = stringResource(R.string.profile_trips_taken))

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        LeaderboardRow(DisplayPicture.DP_DEFAULT, "Jim", 42, 1)

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        LeaderboardRow(DisplayPicture.DP_DEFAULT, "Joris", 38, 2, 4, 0.9F)

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        LeaderboardRow(DisplayPicture.DP_DEFAULT, "Jonas", 34, 3, 8, 0.8F)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                        //TODO Implement functionality to get top 3 people with most sightings.
                        Text(text = stringResource(R.string.profile_species_spotted))

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        LeaderboardRow(DisplayPicture.DP_DEFAULT, "Paddy", 104, 1)

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        LeaderboardRow(DisplayPicture.DP_DEFAULT, "Feargal", 97, 2, 4, 0.9F)

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        LeaderboardRow(DisplayPicture.DP_DEFAULT, "Colm", 82, 3, 8, 0.8F)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        //TODO Implement logout behaviour so the user is returned to
                        // the login page.
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


@Composable
private fun LeaderboardRow(
    displayPicture: DisplayPicture,
    username: String,
    numberToDisplay: Int,
    placement: Int,
    padding: Int = 0,
    scale: Float = 1.0F
) {
    Row(
        modifier = Modifier
            .padding(start = padding.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(displayPicture.defaultResolution),
                contentDescription = stringResource(R.string.display_picture),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(64.dp)
                    .scale(scale)
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
            )

            if (placement == 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
            if (placement == 2) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

            Text(
                text = username.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(numberToDisplay.toString())
    }
}


