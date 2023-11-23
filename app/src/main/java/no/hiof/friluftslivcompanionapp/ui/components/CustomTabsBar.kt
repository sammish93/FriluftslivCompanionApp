package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.models.interfaces.TabNavigation


// Consult this website for additional information:
// https://medium.com/@andkemal/jetpack-compose-and-tab-layout-49f59f8dec29
@Composable
fun CustomTabsBar(viewModel: TabNavigation, navController: NavController, modifier: Modifier = Modifier) {

    val tabsUiState by viewModel.uiState.collectAsState()

    PrimaryTabRow(selectedTabIndex = tabsUiState.currentTabIndex, modifier = modifier) {
        viewModel.tabDestinations.onEachIndexed { index, (destination, title) ->
            Tab(
                text = { Text(stringResource(title)) },
                selected = tabsUiState.currentTabIndex == index,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                onClick = {
                    viewModel.changeHighlightedTab(index)
                    navController.navigate(destination.name) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}