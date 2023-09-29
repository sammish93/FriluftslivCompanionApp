package no.hiof.friluftslivcompanionapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.screens.FloraFaunaScreen
import no.hiof.friluftslivcompanionapp.ui.screens.HomeScreen
import no.hiof.friluftslivcompanionapp.ui.screens.ProfileScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsScreen
import no.hiof.friluftslivcompanionapp.ui.screens.WeatherScreen
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import javax.inject.Inject

import androidx.compose.material3.Typography
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import no.hiof.friluftslivcompanionapp.ui.components.CustomNavigationBar
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.screens.AddScreen
import no.hiof.friluftslivcompanionapp.ui.screens.FloraFaunaSearchScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsAddScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsCreateScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsRecentActivityScreen
import no.hiof.friluftslivcompanionapp.ui.screens.WeatherSearchScreen
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FriluftslivCompanionAppTheme(
                typography = CustomTypography,
                //useDarkTheme = true
            ) {
                Surface(
                    tonalElevation = 5.dp,
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // User is signed in, show the main content of your app
                        // Replace MainContent with your actual main content Composable
                        FriluftslivApp()
                    } else {
                        // User is not signed in, show the sign-in activity
                        SignInActivity()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriluftslivApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute by rememberUpdatedState(
        newValue = navController.currentBackStackEntryAsState().value?.destination?.route
            ?: Screen.HOME.name
    )

    Scaffold(
        bottomBar = {
            CustomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.name,
            modifier = modifier.fillMaxSize()
        ) {
            // Routes go here.
            composable(Screen.HOME.name) {
                HomeScreen(modifier.padding(innerPadding))
            }
            composable(Screen.TRIPS.name) {
                TripsScreen(navController, modifier.padding(innerPadding))
            }
            composable(Screen.WEATHER.name) {
                WeatherScreen(navController, modifier.padding(innerPadding))
            }
            composable(Screen.FLORA_FAUNA.name) {
                FloraFaunaScreen(navController, modifier.padding(innerPadding))
            }
            composable(Screen.PROFILE.name) {
                ProfileScreen(modifier.padding(innerPadding))
            }
            composable(Screen.TRIPS_RECENT_ACTIVITY.name) {
                TripsRecentActivityScreen(navController, modifier.padding(innerPadding))
            }
            composable(Screen.TRIPS_CREATE.name) {
                TripsCreateScreen(navController, modifier.padding(innerPadding))
            }
            composable(Screen.FLORA_FAUNA_SEARCH_LOCATION.name) {
                FloraFaunaSearchScreen("Location", navController, modifier.padding(innerPadding))
            }
            composable(Screen.FLORA_FAUNA_SEARCH_SPECIES.name) {
                FloraFaunaSearchScreen("Species", navController, modifier.padding(innerPadding))
            }
            composable(Screen.WEATHER_SEARCH.name) {
                WeatherSearchScreen(navController, modifier.padding(innerPadding))
            }
        }
    }
}


@Composable
private fun ThemePreview(
    typography: Typography,
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    FriluftslivCompanionAppTheme(typography = CustomTypography, useDarkTheme = isDarkTheme) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun LightThemePreview() {
    Surface(tonalElevation = 5.dp) {
        ThemePreview(typography = CustomTypography, isDarkTheme = false) {
            FriluftslivApp()
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun DarkThemePreview() {
    Surface(tonalElevation = 5.dp) {
        ThemePreview(typography = CustomTypography, isDarkTheme = true) {
            FriluftslivApp()
        }
    }
}