package no.hiof.friluftslivcompanionapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FriluftslivCompanionAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    tonalElevation = 5.dp,
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FriluftslivApp()
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
            // Consult this website for additional information:
            // https://developer.android.com/jetpack/compose/navigation#bottom-nav
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val bottomNavOptions = mapOf(
                    Screen.TRIPS to Icons.Filled.DateRange,
                    Screen.WEATHER to Icons.Filled.LocationOn,
                    Screen.HOME to Icons.Filled.Home,
                    Screen.FLORA_FAUNA to Icons.Filled.Star,
                    Screen.PROFILE to Icons.Filled.AccountCircle
                )

                bottomNavOptions.forEach { screen ->
                    val screenPage = screen.key
                    val icon = screen.value
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = null) },
                        // Displays text if desired.
                        // label = { Text(screen.name) },
                        selected = currentDestination?.hierarchy?.any { it.route == screenPage.name } == true,
                        onClick = {
                            navController.navigate(screenPage.name) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }

            }
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
                TripsScreen(modifier.padding(innerPadding))
            }
            composable(Screen.WEATHER.name) {
                WeatherScreen(modifier.padding(innerPadding))
            }
            composable(Screen.FLORA_FAUNA.name) {
                FloraFaunaScreen(modifier.padding(innerPadding))
            }
            composable(Screen.PROFILE.name) {
                ProfileScreen(modifier.padding(innerPadding))
            }
        }
    }
}



@Composable
private fun ThemePreview(
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    FriluftslivCompanionAppTheme(useDarkTheme = isDarkTheme) {
        content()
    }
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun LightThemePreview() {
    Surface (tonalElevation = 5.dp){
        ThemePreview(isDarkTheme = false) {
            FriluftslivApp()
        }
    }

}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun DarkThemePreview() {
    Surface (tonalElevation = 5.dp){
        ThemePreview(isDarkTheme = true) {
            FriluftslivApp()
        }
    }
}