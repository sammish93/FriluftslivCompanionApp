package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.models.enums.Screen


// Consult this website for additional information:
// https://medium.com/@andkemal/jetpack-compose-and-tab-layout-49f59f8dec29
@Composable
fun CustomTabsBar(titles: Map<Screen, String>, navController: NavController) {

    //var tabIndex by remember {
    //    mutableStateOf(0)
    //}

    // Retrieves the current location (String) - e.g. "HOME"
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    // Retrieves the Enum of the location (Screen) - e.g. Screen.Home
    val routeEnum = Screen.values().firstOrNull() { it.name == currentRoute }
    // Defaults the tab index to 0
    var tabIndex = 0

    // Finds the tab index of the current location
    titles.onEachIndexed { index, (destination, title) ->
        if (destination == routeEnum) {
            tabIndex = index
        }
    }

    TabRow(selectedTabIndex = tabIndex) {
        titles.onEachIndexed { index, (destination, title) ->
            Tab(
                text = { Text(title) },
                selected = tabIndex == index,
                //selected = tabIndex == index,
                onClick = {
                    //tabIndex = index
                    navController.navigate(destination.name) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        //popUpTo(navController.graph.findStartDestination().id) {
                        //    saveState = true
                        //}
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