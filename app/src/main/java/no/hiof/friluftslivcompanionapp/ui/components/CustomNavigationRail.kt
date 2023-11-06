package no.hiof.friluftslivcompanionapp.ui.components

import android.text.Layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
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
fun CustomNavigationRail(navController: NavController) {
    NavigationRail() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val bottomNavOptions = mapOf(
            Screen.TRIPS to painterResource(id = R.drawable.icons8_map_48),
            Screen.WEATHER to painterResource(id = R.drawable.icons8_partly_cloudy_day_48),
            Screen.HOME to painterResource(id = R.drawable.icons8_tent_48),
            Screen.FLORA_FAUNA to painterResource(id = R.drawable.icons8_oak_leaf_48),
            Screen.PROFILE to painterResource(id = R.drawable.icons8_male_user_48)
        )
        bottomNavOptions.forEach { screen ->
            val screenPage = screen.key
            val icon = screen.value

            val contentDescription = when (screenPage) {
                Screen.HOME -> stringResource(R.string.home)
                Screen.TRIPS -> stringResource(R.string.trips)
                Screen.WEATHER -> stringResource(R.string.weather)
                Screen.FLORA_FAUNA -> stringResource(R.string.flora_and_fauna)
                Screen.PROFILE -> stringResource(R.string.profile)
                else -> stringResource(R.string.unknown)
            }

            NavigationRailItem(
                icon = { Icon(icon, contentDescription = contentDescription) },
                label = { Text(stringResource(screenPage.navBarLabel)) },
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