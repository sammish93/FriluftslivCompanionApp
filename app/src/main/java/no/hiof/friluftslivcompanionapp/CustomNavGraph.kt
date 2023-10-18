package no.hiof.friluftslivcompanionapp

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.screens.FloraFaunaAdditionalInfo
import no.hiof.friluftslivcompanionapp.ui.screens.FloraFaunaScreen
import no.hiof.friluftslivcompanionapp.ui.screens.FloraFaunaSearchScreen
import no.hiof.friluftslivcompanionapp.ui.screens.ProfileScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsCreateScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsRecentActivityScreen
import no.hiof.friluftslivcompanionapp.ui.screens.TripsScreen
import no.hiof.friluftslivcompanionapp.ui.screens.WeatherScreen
import no.hiof.friluftslivcompanionapp.ui.screens.WeatherSearchScreen
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.WeatherViewModel

object CustomNavGraph {

    // Animations for page transitions.
    private fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(
        direction: AnimatedContentTransitionScope.SlideDirection,
        duration: Int
    ) =
        slideIntoContainer(
            direction,
            animationSpec = tween(duration)
        )

    private fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
        direction: AnimatedContentTransitionScope.SlideDirection,
        duration: Int
    ) =
        slideOutOfContainer(
            direction,
            animationSpec = tween(duration)
        )

    // Screens accessible by the trips navigation bar button.
    fun NavGraphBuilder.tripsGraph(
        navController: NavController,
        tripsViewModel: TripsViewModel,
        userViewModel: UserViewModel,
        modifier: Modifier
    ) {
        navigation(startDestination = Screen.TRIPS.name, route = Screen.TRIPS.route) {
            // Routes go here.
            composable(
                Screen.TRIPS.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        // Transition animation from every other page.
                        else -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )

                        // Transition animation from every other page.
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {

                // When loop that allows the user to navigate back to the previously visited tab in
                // its specific route. E.g. user navigates to 'Trips', then 'Create Trip', then back
                // to 'Home'. When the user navigates back to 'Trips' they will be navigated to
                // 'Create Trip' by default.
                when (tripsViewModel.getHighlightedTab()) {
                    1 -> {
                        navController.navigate(Screen.TRIPS_RECENT_ACTIVITY.name)
                        {
                            launchSingleTop = true
                        }
                    }

                    2 -> {
                        navController.navigate(Screen.TRIPS_CREATE.name)
                        {
                            launchSingleTop = true
                        }
                    }

                    else -> TripsScreen(navController, modifier, tripsViewModel, userViewModel)
                }
            }

            composable(
                Screen.TRIPS_RECENT_ACTIVITY.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        Screen.TRIPS_CREATE.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        Screen.TRIPS_CREATE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )

                        // Transition animation from every other page.
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {
                TripsRecentActivityScreen(
                    navController,
                    modifier,
                    tripsViewModel
                )
            }

            composable(
                Screen.TRIPS_CREATE.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )

                        // Transition animation from every other page.
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {
                TripsCreateScreen(navController, modifier, tripsViewModel, userViewModel)
            }
        }
    }

    // Screens accessible by the floraFauna navigation bar button.
    fun NavGraphBuilder.floraFaunaGraph(
        navController: NavController,
        floraFaunaViewModel: FloraFaunaViewModel,
        userViewModel: UserViewModel,
        modifier: Modifier
    ) {
        navigation(startDestination = Screen.FLORA_FAUNA.name, route = Screen.FLORA_FAUNA.route) {
            // Routes go here.
            composable(
                Screen.FLORA_FAUNA.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        // Transition animation from every other page.
                        else -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
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
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {

                // When loop that allows the user to navigate back to the previously visited tab in
                // its specific route. E.g. user navigates to 'Trips', then 'Create Trip', then back
                // to 'Home'. When the user navigates back to 'Trips' they will be navigated to
                // 'Create Trip' by default.
                when (floraFaunaViewModel.getHighlightedTab()) {
                    1 -> {
                        navController.navigate(Screen.FLORA_FAUNA_SEARCH_LOCATION.name)
                        {
                            launchSingleTop = true
                        }
                    }

                    2 -> {
                        navController.navigate(Screen.FLORA_FAUNA_SEARCH_SPECIES.name)
                        {
                            launchSingleTop = true
                        }
                    }

                    else -> FloraFaunaScreen(navController, modifier, floraFaunaViewModel, userViewModel)
                }
            }

            composable(
                Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )
                        // Transition animation from every other page.
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {
                FloraFaunaSearchScreen(
                    "Location",
                    navController,
                    modifier,
                    floraFaunaViewModel,
                    userViewModel
                )
            }

            composable(
                Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.PROFILE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )

                        // Transition animation from every other page.
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {
                FloraFaunaSearchScreen(
                    "Species",
                    navController,
                    modifier,
                    floraFaunaViewModel,
                    userViewModel
                )
            }
            // FLORA_FAUNA_ADDITIONAL_INFO-skjermen
            composable(
                Screen.FLORA_FAUNA_ADDITIONAL_INFO.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        Screen.FLORA_FAUNA.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        else -> null
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        Screen.FLORA_FAUNA.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        else -> null
                    }
                }
            ) {
                // Innholdet for FLORA_FAUNA_ADDITIONAL_INFO-skjermen
                FloraFaunaAdditionalInfo(
                    modifier,
                    floraFaunaViewModel
                )
            }

        }
    }

    // Screens accessible by the weather navigation bar button.
    fun NavGraphBuilder.weatherGraph(
        navController: NavController,
        weatherViewModel: WeatherViewModel,
        userViewModel: UserViewModel,
        modifier: Modifier
    ) {
        navigation(startDestination = Screen.WEATHER.name, route = Screen.WEATHER.route) {
            // Routes go here.
            composable(
                Screen.WEATHER.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.WEATHER_SEARCH.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        // Transition animation from every other page.
                        else -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.WEATHER_SEARCH.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.PROFILE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )

                        // Transition animation from every other page.
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {

                // When loop that allows the user to navigate back to the previously visited tab in
                // its specific route. E.g. user navigates to 'Trips', then 'Create Trip', then back
                // to 'Home'. When the user navigates back to 'Trips' they will be navigated to
                // 'Create Trip' by default.
                when (weatherViewModel.getHighlightedTab()) {
                    1 -> {
                        navController.navigate(Screen.WEATHER_SEARCH.name)
                        {
                            launchSingleTop = true
                        }
                    }

                    else -> WeatherScreen(navController, modifier, weatherViewModel, userViewModel)
                }
            }

            composable(
                Screen.WEATHER_SEARCH.name,
                enterTransition = {
                    when (initialState.destination.route) {
                        // Transition animation from the following pages.
                        Screen.WEATHER.name -> enterTransition(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            500
                        )

                        // No transition animation from other pages.
                        else -> null
                    }
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        // Transition animation from the following pages.
                        Screen.WEATHER.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            500
                        )

                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.TRIPS.name, Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name,
                        Screen.PROFILE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )

                        // Transition animation from every other page.
                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {
                WeatherSearchScreen(navController, modifier, weatherViewModel)
            }
        }
    }

    // Screens accessible by the profile navigation bar button.
    fun NavGraphBuilder.profileGraph(
        navController: NavController,
        userViewModel: UserViewModel,
        modifier: Modifier
    ) {
        navigation(startDestination = Screen.PROFILE.name, route = Screen.PROFILE.route) {
            // Routes go here.
            composable(
                Screen.PROFILE.name,
                enterTransition = {
                    // Transition animation from every page.
                    enterTransition(AnimatedContentTransitionScope.SlideDirection.Up, 500)
                },
                exitTransition = {
                    when (navController.currentBackStackEntry?.destination?.route) {
                        Screen.FLORA_FAUNA.name,
                        Screen.FLORA_FAUNA_SEARCH_LOCATION.name,
                        Screen.FLORA_FAUNA_SEARCH_SPECIES.name,
                        Screen.WEATHER.name,
                        Screen.WEATHER_SEARCH.name,
                        Screen.TRIPS.name,
                        Screen.TRIPS_RECENT_ACTIVITY.name,
                        Screen.TRIPS_CREATE.name -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            500
                        )

                        else -> exitTransition(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            500
                        )
                    }
                }) {
                ProfileScreen()
            }
        }
    }
}