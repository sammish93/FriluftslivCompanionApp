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

    private val api = BirdObservations.getInstance()

    private val _birdResults = MutableStateFlow<List<Bird>>(emptyList())
    val birdResults: StateFlow<List<Bird>> = _birdResults

    /**
     * Updates the bird results with the provided list of bird observations.
     *
     * This method sets the bird results to the provided list of bird observations,
     * updating the state to reflect the latest bird data.
     *
     * @param results The list of bird observations to update the bird results with.
     */
    private fun updateBirdResults(results: List<Bird>) {
        _birdResults.value = results
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //TODO: Solve why it returns more or less than the set maxResult
    //TODO: Solve why it only returns 1 result when using my location

    /**
     * Searches for birds based on the specified location.
     *
     * This method makes an asynchronous API call to retrieve recent bird observations
     * for the given location, processes the results, and updates the bird results.
     *
     * @param location The location or region code to search for bird observations.
     */
    suspend fun searchBirdsByLocation(location: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val result = api.getRecentObservations(regionCode = location, maxResult = 10)

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
            } finally { _isLoading.value = false }
        }
    }


    private suspend fun performSecondaryRequest(location: String): Result<List<Bird>> {
        println("No enough bird observations found for the specified location. Making a secondary request...")

        return api.getObservationsBetweenDates(
            startDate = LocalDate.now().minusDays(2),
            endDate = LocalDate.now(),
            regionCode = location,
            maxResult = 5
        )
    }


    fun searchBirdsByYourLocation(location: String): Pair<String, String> {
        return try {
            val regionCode = when (location) {
                //I need the previous region code and the current region name,
                // as Ebird uses the previous region codes,
                // and the geocode API uses the current region name.

                "Østfold" -> "NO-01"
                "Buskerud" -> "NO-06"
                "Akershus" -> "NO-02"
                "Viken" -> "NO-01,NO-02,NO-06"

                "Hedemark" -> "NO-04"
                "Oppland" -> "NO-05"
                "Innlandet" -> "NO-04,NO-05"

                "Oslo" -> "NO-03"

                "Telemark" -> "NO-08"
                "Vestfold" -> "NO-07"
                "Vestfold og Telemark" -> "NO-07,NO-08"

                "Aust-Agder" -> "NO-09"
                "Vest-Agder" -> "NO-10"
                "Agder" -> "NO-09, NO-10"

                "Rogaland" -> "NO-11"

                "Hordaland" -> "NO-12"
                "Sogn og Fjordane" -> "NO-14"
                "Vestland" -> "NO-12, NO-14"

                "Møre og Romsdal" -> "NO-15"

                "Sør-Trøndelag" -> "NO-16"
                "Nord-Trøndelag" -> "NO-17"
                "Trøndelag" -> "NO-16,NO-17"

                "Nordland" -> "NO-18"

                "Troms" -> "NO-19"
                "Finnmark" -> "NO-20"
                "Troms og Finnmark" -> "NO-19,NO-20"

                else -> "NO-03"
            }
            regionCode to "Success" // Returner både regionkoden og en suksessmelding
        } catch (e: Exception) {
            println("Error: ${e.message}")
            "NO-03" to "Error: ${e.message}" // Standardverdi og en feilmelding i tilfelle det oppstår feil
        }
    }


    private val _selectedBirdInfo = MutableStateFlow<BirdInfo?>(null)
    val selectedBirdInfo: StateFlow<BirdInfo?> = _selectedBirdInfo

    /**
     * Method to update the information about the selected bird.
     *
     * @param bird The bird to update with new information.
     */
    fun updateSelectedBirdInfo(bird: Bird) {
        val birdInfo = bird.getBirdInfo()
        _selectedBirdInfo.value = birdInfo
    }
}