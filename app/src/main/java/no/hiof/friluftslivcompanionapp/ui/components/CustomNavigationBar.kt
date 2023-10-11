package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.Screen


// Consult this website for additional information:
// https://developer.android.com/jetpack/compose/navigation#bottom-nav
@Composable
fun CustomNavigationBar(navController: NavController) {
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

            val contentDescription = when (screenPage) {
                Screen.TRIPS -> stringResource(R.string.trips)
                Screen.WEATHER -> stringResource(R.string.weather)
                Screen.HOME -> stringResource(R.string.home)
                Screen.FLORA_FAUNA -> stringResource(R.string.flora_and_fauna)
                Screen.PROFILE -> stringResource(R.string.profile)
                else -> stringResource(R.string.unknown)
            }
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = contentDescription) },
                //TODO if test to determine whether to display navbar label in English or Norwegian.
                label = { Text(screenPage.navBarLabelEn) },
                selected = currentDestination?.hierarchy?.any { it.route == screenPage.route } == true,
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