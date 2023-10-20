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
import no.hiof.friluftslivcompanionapp.domain.Geocoding
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.BirdInfo
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
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

                // COMMENTS TO LORENA
                // Call this:
                val result = api.getRecentObservations(regionCode = location, maxResult = 10)
                // If list is empty then call this:
                /*
                result = api.getObservationsBetweenDates(
                    startDate = LocalDate.now().minusWeeks(1),
                    endDate = LocalDate.now(),
                    regionCode = "NO-15",
                    maxResult = 10
                )
                 */
                // If list is still empty then maybe just create a boolean mutablestateof and make it
                // so that a message appears that says 'no birds found recently in your area'
                // you can use my weatherscreen as a basis - it displays a loading screen when
                // the api request is being sent. once it is returned then the contents are dispalyed.
                // if your list is empty then instead of showing contents you could show just a simple
                // text - like my weather screen shows 'no gps location found'.

                if (result is Result.Success) {
                    val birdList = result.value
                    if (birdList.isNotEmpty()) {
                        val processedList = api.processBirdList(birdList) { bird ->
                            bird
                        }
                        updateBirdResults(processedList)
                    } else {
                        println("No bird observations found for the specified location.")
                    }
                } else if (result is Result.Failure) {
                    println("API call failed: ${result.message}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
            }

        }
    }


    suspend fun searchBirdsByYourLocation(location: String): Pair<String, String> {
        return try {
            val regionCode = when (location) {
                "Oslo" -> "NO-03"
                "Viken" -> "NO-30"
                "Vestland" -> "NO-46"
                "Agder" -> "NO-42"
                "Innlandet" -> "NO-34"
                "Rogaland" -> "NO-11"
                "Vestfold og Telemark" -> "NO-38"
                "Møre og Romsdal" -> "NO-15"
                "Trøndelag" -> "NO-50"
                "Troms og Finnmark" -> "NO-54"
                "Nordland" -> "NO-18"
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