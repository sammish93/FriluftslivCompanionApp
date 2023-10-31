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
import no.hiof.friluftslivcompanionapp.data.states.FloraFaunaState
import no.hiof.friluftslivcompanionapp.data.states.WeatherState
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.BirdInfo
import java.time.LocalDate

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

) : ViewModel(), TabNavigation {

    private val _uiState = MutableStateFlow(TabsUiState())
    override val uiState: StateFlow<TabsUiState> = _uiState.asStateFlow()

    private val _floraFaunaState = MutableStateFlow(FloraFaunaState())
    val floraFaunaState: StateFlow<FloraFaunaState> = _floraFaunaState.asStateFlow()

    private val api = BirdObservations.getInstance()

    override var tabDestinations = mapOf(
        Screen.FLORA_FAUNA to Screen.FLORA_FAUNA.navBarLabel,
        Screen.FLORA_FAUNA_SEARCH_LOCATION to Screen.FLORA_FAUNA_SEARCH_LOCATION.navBarLabel
    )

    override fun changeHighlightedTab(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTabIndex = index
            )
        }
    }

    // Function to retrieve the last highlighted tab.
    override fun getHighlightedTab(): Int {
        return _uiState.value.currentTabIndex
    }

    /**
     * Updates the bird results with the provided list of bird observations.
     *
     * This method sets the bird results to the provided list of bird observations,
     * updating the state to reflect the latest bird data.
     *
     * @param results The list of bird observations to update the bird results with.
     */
    private fun updateBirdResults(results: List<Bird>) {
        _floraFaunaState.update { currentState ->
            currentState.copy(
                birdResults = results
            )
        }
    }

    /**
     * Searches for birds based on the specified location.
     *
     * This method makes an asynchronous API call to retrieve recent bird observations
     * for the given location, processes the results, and updates the bird results.
     *
     * @param location The location or region code to search for bird observations.
     */
    suspend fun searchBirdsByLocation(location: String, maxResults: Int = 20) {
        viewModelScope.launch {
            try {
                updateLoadingBirdResponse(true)

                val result = api.getRecentObservations(regionCode = location, maxResult = maxResults)

                if (result is Result.Success) {
                    val birdList = result.value
                    if (birdList.isNotEmpty()) {
                        val processedList = api.processBirdList(birdList) { bird ->
                            bird
                        }
                        updateBirdResults(processedList)
                    } else {
                        val secondaryResult = performSecondaryRequest(location)

                        if (secondaryResult is Result.Success) {
                            val secondaryBirdList = secondaryResult.value
                            if (secondaryBirdList.isNotEmpty()) {
                                val processedList = api.processBirdList(secondaryBirdList) { bird ->
                                    bird
                                }
                                updateBirdResults(processedList)
                            } else {
                                println("No bird observations found in the past week in the specified location.")
                            }
                        } else if (secondaryResult is Result.Failure) {
                            println("Secondary API call failed: ${secondaryResult.message}")
                        }
                    }
                } else if (result is Result.Failure) {
                    println("API call failed: ${result.message}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            } finally { updateLoadingBirdResponse(false) }
        }
    }

    private suspend fun performSecondaryRequest(location: String, maxResults: Int = 20): Result<List<Bird>> {
        println("No enough bird observations found for the specified location. Making a secondary request...")

        return api.getObservationsBetweenDates(
            startDate = LocalDate.now().minusWeeks(1),
            endDate = LocalDate.now(),
            regionCode = location,
            maxResult = maxResults
        )
    }

    /**
     * Method to update the information about the selected bird.
     *
     * @param bird The bird to update with new information.
     */
    fun updateSelectedBirdInfo(bird: Bird) {
        val birdInfo = bird.getBirdInfo()

        _floraFaunaState.update { currentState ->
            currentState.copy(
                selectedBirdInfo = birdInfo
            )
        }
    }

    // Changes a Boolean value used to show/hide a progress bar.
    fun updateLoadingBirdResponse(isLoading: Boolean) {
        _floraFaunaState.update { currentState ->
            currentState.copy(
                isLoading = isLoading
            )
        }
    }
}