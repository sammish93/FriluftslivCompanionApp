package no.hiof.friluftslivcompanionapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import no.hiof.friluftslivcompanionapp.ui.screens.HomeScreen
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import javax.inject.Inject

import androidx.compose.material3.Typography
import no.hiof.friluftslivcompanionapp.ui.components.CustomNavigationBar
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import androidx.hilt.navigation.compose.hiltViewModel
import no.hiof.friluftslivcompanionapp.data.managers.LocationManager
import no.hiof.friluftslivcompanionapp.data.managers.PermissionManager
import no.hiof.friluftslivcompanionapp.CustomNavGraph.floraFaunaGraph
import no.hiof.friluftslivcompanionapp.CustomNavGraph.profileGraph
import no.hiof.friluftslivcompanionapp.CustomNavGraph.tripsGraph
import no.hiof.friluftslivcompanionapp.CustomNavGraph.weatherGraph
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.WeatherViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by viewModels()

    // Initialize location manager and update viewModel.
    private val locationManager = LocationManager(this, lifecycle) { location ->
        userViewModel.updateLocation(location)
    }

    private val permissionManager by lazy {
        PermissionManager(
            activityResultRegistry,
            onPermissionGranted = { locationManager.startLocationUpdate() },
            onPermissionDenied = {
            /* Can handle denied permission if needed. I don´t know how we will do this yet. */
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationManager.initializeFusedLocationProviderClient()

        // Check for location permissions and request if not granted.
        if (!locationManager.hasLocationPermission()) {
            permissionManager.requestPermission(ACCESS_FINE_LOCATION)
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {

            // Updates the userViewModel to the current user.
            userViewModel.updateCurrentUser(currentUser)

            // User is signed in, shows the main content.
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
                        FriluftslivApp(userViewModel = userViewModel)
                    }
                }
            }
        } else {
            // No user signed in, start SignInActivity
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
            finish()
        }
    }


}


@Composable
fun FriluftslivApp(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
) {
    val navController = rememberNavController()
    val currentRoute by rememberUpdatedState(
        newValue = navController.currentBackStackEntryAsState().value?.destination?.route
            ?: Screen.HOME.name
    )

    // ViewModels go here.
    val tripsViewModel = hiltViewModel<TripsViewModel>()
    val floraFaunaViewModel = hiltViewModel<FloraFaunaViewModel>()
    val weatherViewModel = hiltViewModel<WeatherViewModel>()

    // CustomTabsBar Composables are assigned to functions here and injected in NavHost below.
    val tripsTabsBar: @Composable () -> Unit =
        { CustomTabsBar(tripsViewModel, navController) }
    val floraFaunaTabsBar: @Composable () -> Unit =
        { CustomTabsBar(floraFaunaViewModel, navController) }
    val weatherTabsBar: @Composable () -> Unit =
        { CustomTabsBar(weatherViewModel, navController) }

    Scaffold(
        topBar = {
            when (currentRoute) {
                // The following pages cause a tab bar to appear.
                Screen.TRIPS.name,
                Screen.TRIPS_RECENT_ACTIVITY.name,
                Screen.TRIPS_CREATE.name -> tripsTabsBar()

                Screen.WEATHER.name,
                Screen.WEATHER_SEARCH.name -> weatherTabsBar()

                Screen.FLORA_FAUNA.name,
                Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                Screen.FLORA_FAUNA_SEARCH_SPECIES.name -> floraFaunaTabsBar()

                // No tab bars for every other page.
                else -> null
            }
        },
        bottomBar = {
            CustomNavigationBar(navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.HOME.name,
                modifier = modifier.fillMaxSize()
            ) {
                composable(
                    Screen.HOME.name,
                    enterTransition = {
                        // Transition animation from every page.
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )
                    }) {
                    HomeScreen(modifier.padding(innerPadding))
                }

                tripsGraph(navController, tripsViewModel, userViewModel, modifier)

                floraFaunaGraph(navController, floraFaunaViewModel, userViewModel, modifier)

                weatherGraph(navController, weatherViewModel, userViewModel, modifier)

                profileGraph(navController, userViewModel, modifier)
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
            // FriluftslivApp()
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun DarkThemePreview() {
    Surface(tonalElevation = 5.dp) {
        ThemePreview(typography = CustomTypography, isDarkTheme = true) {
            // FriluftslivApp()
        }
    }
}
