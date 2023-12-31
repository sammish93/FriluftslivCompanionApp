package no.hiof.friluftslivcompanionapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import no.hiof.friluftslivcompanionapp.CustomNavGraph.floraFaunaGraph
import no.hiof.friluftslivcompanionapp.CustomNavGraph.profileGraph
import no.hiof.friluftslivcompanionapp.CustomNavGraph.tripsGraph
import no.hiof.friluftslivcompanionapp.CustomNavGraph.weatherGraph
import no.hiof.friluftslivcompanionapp.data.managers.LocationManager
import no.hiof.friluftslivcompanionapp.data.managers.PermissionManager
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.CustomLoadingScreen
import no.hiof.friluftslivcompanionapp.ui.components.CustomNavigationBar
import no.hiof.friluftslivcompanionapp.ui.components.CustomNavigationRail
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.screens.HomeScreen
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.WeatherViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by viewModels()

    // Initialize location manager and update viewModel.
    private val locationManager = LocationManager(this, lifecycle) { location ->
        userViewModel.updateLocation(location)
        userViewModel.updateLocationPermissionGranted(true)
        userViewModel.updateLocationManagerCalled(true)
    }

    private val permissionManager by lazy {
        PermissionManager(
            activityResultRegistry,
            onPermissionGranted = { locationManager.startLocationUpdate() },
            onPermissionDenied = {
                /*
                val defaultLoc = android.location.Location("Permission Manager - Denied")
                defaultLoc.longitude = DefaultLocation.OSLO.lon
                defaultLoc.latitude = DefaultLocation.OSLO.lat
                userViewModel.updateLocation(defaultLoc)
                 */
                userViewModel.updateLocationManagerCalled(true)
            }
        )
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hides the "Friluftsliv Companion" black bar that appears at the top of the screen.
        supportActionBar?.hide()

        locationManager.initializeFusedLocationProviderClient()

        // Check for location permissions and request if not granted.
        if (!locationManager.hasLocationPermission()) {
            permissionManager.requestPermission(ACCESS_FINE_LOCATION)
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {

            // Updates the userViewModel to the current user.
            //TODO: Update values for isDarkMode, language, etc.
            userViewModel.updateCurrentUser(currentUser)
            userViewModel.fetchDarkModePreference(currentUser.uid)
            userViewModel.fetchDisplayPicture(currentUser.uid)
            userViewModel.fetchUserLanguagePreference(currentUser.uid)



            // User is signed in, shows the main content.
            setContent {
                // State to be present in a composable so that theme updates on value change.
                val userState by userViewModel.state.collectAsState()

                // Updates the screen's current window size (even when rotated/resized).
                userViewModel.updateWindowSizeClass(calculateWindowSizeClass(this))

                FriluftslivCompanionAppTheme(
                    typography = CustomTypography,
                    useDarkTheme = userState.isDarkMode
                ) {
                    Surface(
                        tonalElevation = 5.dp,
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        FriluftslivApp(userViewModel = userViewModel)
                    }
                }

                // Code was based on examples shown here -
                // https://medium.com/@fierydinesh/multi-language-support-android-localization-in-app-and-system-settings-change-language-e00957e9c48c
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(userViewModel.getLanguage().code))
            }
        } else {
            // No user signed in, start SignInActivity
            val signInIntent = Intent(this, SignInActivity::class.java)
            startActivity(signInIntent)
            finish()
        }
    }
}

// Screen used when a user's GPS location is being fetched.
@Composable
fun WaitingScreen(userViewModel: UserViewModel = viewModel()) {

    LaunchedEffect(true) {
        // If the user's location is switched off then access to the application will be given
        // after 5 seconds. This prevents an infinite loop.
        delay(5000)
        userViewModel.updateLocationManagerCalled(true)
    }

    CustomLoadingScreen()
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
    val userState by userViewModel.state.collectAsState()

    // Navigation bars. Bottom bar is shown in phone portrait. Everything else generally uses
    // landscape.
    val bottomBarShown = remember { mutableStateOf(userState.windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) }
    val railBarShown = remember { mutableStateOf(userState.windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) }

    // CustomTabsBar Composables are assigned to functions here and injected in NavHost below.
    val tripsTabsBar: @Composable () -> Unit =
        { CustomTabsBar(tripsViewModel, navController,
            Modifier.padding(start =
            if (userState.windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact &&
                userState.isRailBarOpened) 80.dp else 0.dp)) }
    val floraFaunaTabsBar: @Composable () -> Unit =
        { CustomTabsBar(floraFaunaViewModel, navController,
            Modifier.padding(start =
            if (userState.windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact &&
                userState.isRailBarOpened) 80.dp else 0.dp)) }
    val weatherTabsBar: @Composable () -> Unit =
        { CustomTabsBar(weatherViewModel, navController,
            Modifier.padding(start =
            if (userState.windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact &&
                userState.isRailBarOpened) 80.dp else 0.dp)) }

    when (userState.isLocationManagerCalled) {
        true ->
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
                    when (bottomBarShown.value) {
                        // Shows bottom nav bar.
                        true -> CustomNavigationBar(navController)

                        else -> {}
                    }
                },
                floatingActionButton = {
                    when (bottomBarShown.value) {
                        false -> {
                            // Shows button which allows side nav bar to be shown on click. Button
                            // is hidden when side nav bar is already shown.
                            when (userState.isRailBarOpened) {
                                false -> FloatingActionButton(
                                    onClick = {
                                        userViewModel.updateIsRailBarOpened(true)
                                    }
                                ) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                                }

                                else -> {}
                            }
                        }

                        else -> {}
                    }
                },
                floatingActionButtonPosition = FabPosition.Start
            ) { innerPadding ->

                when (railBarShown.value) {
                    // Shows side nav bar.
                    true -> CustomNavigationRail(navController, userViewModel)

                    else -> {}
                }

                Box(modifier = Modifier.padding(innerPadding)) {

                    Row(modifier = Modifier.padding(start = if (railBarShown.value && userState.isRailBarOpened) 80.dp else 0.dp)) {
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
                                HomeScreen(
                                    navController,
                                    userViewModel,
                                    tripsViewModel,
                                    floraFaunaViewModel,
                                    modifier.padding(innerPadding)
                                )
                            }

                            tripsGraph(navController, tripsViewModel, userViewModel, modifier)

                            floraFaunaGraph(
                                navController,
                                floraFaunaViewModel,
                                userViewModel,
                                modifier
                            )

                            weatherGraph(navController, weatherViewModel, userViewModel, modifier)

                            profileGraph(navController, userViewModel, modifier)
                        }
                    }
                }
            }

        else -> WaitingScreen(userViewModel)
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
