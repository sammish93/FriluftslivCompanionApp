package no.hiof.friluftslivcompanionapp.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.data.states.TabsUiState
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.interfaces.TabNavigation
import javax.inject.Inject
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.models.Bird
//import no.hiof.friluftslivcompanionapp.data.repositories.FloraFaunaRepository

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
class FloraFaunaViewModel @Inject constructor(
    // Communication with the data layer can be injected as dependencies here.
    // private val repository: TripsRepository
    //private val repository: FloraFaunaRepository

) : ViewModel(), TabNavigation {

    private val _uiState = MutableStateFlow(TabsUiState())
    override val uiState: StateFlow<TabsUiState> = _uiState.asStateFlow()

    override var tabDestinations = mapOf(
        Screen.FLORA_FAUNA to "Lifelist",
        Screen.FLORA_FAUNA_SEARCH_LOCATION to "Search (By Location)",
        Screen.FLORA_FAUNA_SEARCH_SPECIES to "Search (By Species)"
    )
    override fun changeHighlightedTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTabIndex = index
            )
        }
    }


    private val api = BirdObservations.getInstance()

    private val _birdResults = MutableStateFlow<List<Bird>>(emptyList())
    val birdResults: StateFlow<List<Bird>> = _birdResults
    //.asStateFlow?
    private fun updateBirdResults(results: List<Bird>) {
        _birdResults.value=results
    }

    suspend fun searchBirdsByLocation(location: String) {
        viewModelScope.launch {
            try {
                val result = api.getRecentObservations(regionCode = location)
                if (result is Result.Success) {
                    val birdList = result.value
                    val processedList = api.processBirdList(birdList) { bird ->
                        bird
                    }
                    updateBirdResults(processedList)
                } else if (result is Result.Failure) {
                    println("API call failed: ${result.message}")
                }
            } catch(e: Exception) {
                println("Error: ${e.message}")
            }

        }
    }
}