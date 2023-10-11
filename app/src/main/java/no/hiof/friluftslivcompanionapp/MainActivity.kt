package no.hiof.friluftslivcompanionapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import no.hiof.friluftslivcompanionapp.ui.screens.FloraFaunaScreen
import no.hiof.friluftslivcompanionapp.ui.screens.HomeScreen
import no.hiof.friluftslivcompanionapp.ui.screens.ProfileScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsScreen
import no.hiof.friluftslivcompanionapp.ui.screens.WeatherScreen
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import javax.inject.Inject

import androidx.compose.material3.Typography
import androidx.core.content.ContextCompat
import no.hiof.friluftslivcompanionapp.ui.components.CustomNavigationBar
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.screens.FloraFaunaSearchScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsCreateScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsRecentActivityScreen
import no.hiof.friluftslivcompanionapp.ui.screens.WeatherSearchScreen
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMapState
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.MapViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.WeatherViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth


    // Google Maps
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->

            if (isGranted) {
                viewModel.getDeviceLocation(fusedLocationProviderClient)
            }
        }

    private fun askPermissions() = when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION
        ) -> {
            viewModel.getDeviceLocation(fusedLocationProviderClient)
        }
        else -> {
            requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()
    // Google Maps.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Google Maps.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()

        val currentUser = auth.currentUser
        if (currentUser != null) {

            // User is signed in, shows the main content
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
                        FriluftslivApp(state = viewModel.state.value)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriluftslivApp(
    modifier: Modifier = Modifier,
    state: GoogleMapState
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
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.name,
            modifier = modifier.fillMaxSize()
        ) {
            // Routes go here.
            composable(Screen.HOME.name,
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
            composable(Screen.TRIPS.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                TripsScreen(navController, modifier.padding(innerPadding), tripsViewModel, mapState = state)
            }
            composable(Screen.WEATHER.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.WEATHER_SEARCH.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.WEATHER_SEARCH.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                WeatherScreen(navController, modifier.padding(innerPadding), weatherViewModel)
            }
            composable(Screen.FLORA_FAUNA.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                FloraFaunaScreen(navController, modifier.padding(innerPadding), floraFaunaViewModel)
            }
            composable(Screen.PROFILE.name,
                enterTransition = {
                    // Transition animation from every page.
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    when (currentRoute) {
                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                ProfileScreen(modifier.padding(innerPadding))
            }
            composable(Screen.TRIPS_RECENT_ACTIVITY.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        Screen.TRIPS_CREATE.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        Screen.TRIPS_CREATE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                TripsRecentActivityScreen(
                    navController,
                    modifier.padding(innerPadding),
                    tripsViewModel
                )
            }
            composable(Screen.TRIPS_CREATE.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                TripsCreateScreen(navController, modifier.padding(innerPadding), tripsViewModel)
            }
            composable(Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                FloraFaunaSearchScreen(
                    "Location",
                    navController,
                    modifier.padding(innerPadding),
                    floraFaunaViewModel
                )
            }
            composable(Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                FloraFaunaSearchScreen(
                    "Species",
                    navController,
                    modifier.padding(innerPadding),
                    floraFaunaViewModel
                )
            }
            composable(Screen.WEATHER_SEARCH.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.WEATHER.name -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (currentRoute) {
                        // Transition animation from the following pages.
                        Screen.WEATHER.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.TRIPS.name, Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.PROFILE.name -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(500)
                        )

                        // Transition animation from every other page.
                        else -> slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(500)
                        )
                    }
                }) {
                WeatherSearchScreen(navController, modifier.padding(innerPadding), weatherViewModel)
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
