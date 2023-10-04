package no.hiof.friluftslivcompanionapp.viewmodels

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.hiof.friluftslivcompanionapp.data.TabsUiState
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.interfaces.TabNavigation
import no.hiof.friluftslivcompanionapp.ui.screens.TripsScreen
import javax.inject.Inject

// NOTE: Composable Screens in app/ui/screens can communicate with this viewmodel (and thus the data
// layer via 'import androidx.lifecycle.viewmodel.compose.viewModel' at the top of the file, and
// then adding 'viewModel: TripsViewModel = viewModel()' in the constructor.
// Afterwards, go to the relevant activity (in this case MainActivity), navigate to the NavHost
// section where all navigation routes are defined, and update it to something like this:
//composable(Screen.TRIPS.name) { backStackEntry ->
//    val viewModel = hiltViewModel<TripsViewModel>()
//    TripsScreen(navController, modifier.padding(innerPadding), viewModel)
//}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    // Communication with the data layer can be injected as dependencies here.
    // private val repository: TripsRepository
) : ViewModel(), TabNavigation {

    private val _uiState = MutableStateFlow(TabsUiState())
    override val uiState: StateFlow<TabsUiState> = _uiState.asStateFlow()

    override var tabDestinations = mapOf(
        Screen.WEATHER to "Weather",
        Screen.WEATHER_SEARCH to "Search"
    )

    override fun changeHighlightedTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTabIndex = index
            )
        }
    }
}