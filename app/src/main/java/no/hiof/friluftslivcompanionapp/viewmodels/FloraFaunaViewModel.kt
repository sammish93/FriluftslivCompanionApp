package no.hiof.friluftslivcompanionapp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.GeoPoint
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
import no.hiof.friluftslivcompanionapp.data.repositories.LifelistRepository
import no.hiof.friluftslivcompanionapp.data.repositories.OperationResult
import no.hiof.friluftslivcompanionapp.data.states.FloraFaunaState
import no.hiof.friluftslivcompanionapp.data.states.LifeListState

import no.hiof.friluftslivcompanionapp.domain.FloraFaunaFactory
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.FloraFaunaSighting
import no.hiof.friluftslivcompanionapp.models.Lifelist
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import java.time.Duration
import java.time.LocalDate
import java.util.Date

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

    private val lifelistRepository: LifelistRepository

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
     * Updates the species results with the provided list of species observations.
     *
     * This method sets the species results to the provided list of species observations,
     * updating the state to reflect the latest species data.
     *
     * @param results The list of species observations to update the species results with.
     */
    private fun updateSpeciesResults(results: List<FloraFauna>) {
        _floraFaunaState.update { currentState ->
            currentState.copy(
                speciesResults = results
            )
        }
    }

    /**
     * Searches for species based on the specified location.
     *
     * This method makes an asynchronous API call to retrieve recent species observations
     * for the given location, processes the results, and updates the species results.
     *
     * @param location The location or region code to search for species observations.
     */
    suspend fun searchSpeciesByLocation(
        location: String,
        maxResults: Int = 20,
        language: SupportedLanguage
    ) {
        viewModelScope.launch {
            updateSpeciesResults(emptyList())

            try {
                updateLoadingSpeciesResponse(true)

                val result = performSecondaryRequest(
                    language = language,
                    location = location,
                    maxResults = maxResults
                )

                if (result is Result.Success) {
                    val speciesList = result.value
                    if (speciesList.isNotEmpty()) {
                        val processedList = api.processBirdList(speciesList) { species ->
                            species
                        }
                        updateSpeciesResults(processedList)
                    } else {
                        Log.i("searchSpeciesByLocation",
                            "No species observations found in the past week in the specified location.")
                    }
                } else if (result is Result.Failure) {
                    Log.e("searchSpeciesByLocation", "API call failed: ${result.message}")
                }
            } catch (e: Exception) {
                Log.e("searchSpeciesByLocation", "Error: ${e.message}")
            } finally {
                updateLoadingSpeciesResponse(false)
            }
        }
    }

    private suspend fun performSecondaryRequest(
        location: String,
        language: SupportedLanguage,
        maxResults: Int = 20
    ): Result<List<Bird>> {
        Log.i("SecondaryRequest",
            "No enough species observations found for the specified location. Making a secondary request...")


        return api.getObservationsBetweenDates(
            languageCode = language,
            startDate = LocalDate.now().minusWeeks(1),
            endDate = LocalDate.now(),
            regionCode = location,
            maxResult = maxResults
        )
    }

    /**
     * Method to update the information about the selected species.
     *
     * @param species The species to update with new information.
     */
    fun updateSelectedSpeciesInfo(species: FloraFauna) {
        if (species is Bird) {
            _floraFaunaState.update { currentState ->
                currentState.copy(
                    selectedSpecies = species
                )
            }
        }
    }

    // Changes a Boolean value used to show/hide a progress bar.
    fun updateLoadingSpeciesResponse(isLoading: Boolean) {
        _floraFaunaState.update { currentState ->
            currentState.copy(
                isLoading = isLoading
            )
        }
    }

    // Updates a Date value to be used when adding a sighting.
    fun updateSightingDate(date: Date) {
        _floraFaunaState.update { currentState ->
            currentState.copy(
                sightingDate = date
            )
        }
    }

    // Updates a Location value to be used when adding a sighting.
    fun updateSightingLocation(location: Location) {
        _floraFaunaState.update { currentState ->
            currentState.copy(
                sightingLocation = location
            )
        }
    }

    // Creates a sighting based on state values and then writes to the database.
    fun createSighting() {
        val floraFaunaState = _floraFaunaState.value


        val sightingDate = floraFaunaState.sightingDate
        val sightingLocation = floraFaunaState.sightingLocation

        floraFaunaState.selectedSpecies?.let { selectedSpecies ->
            val sighting = FloraFaunaFactory.createSighting(selectedSpecies, sightingDate, sightingLocation)

            sighting?.let { newSighting ->
                viewModelScope.launch {
                    try {
                        lifelistRepository.addSightingToLifeList(newSighting)

                        clearSighting()
                    } catch (e: Exception) {
                        Log.e("addSightingToLifeList", "Error add sighting to life list: ${e.message}")

                    }
                }
            }
        }
    }

    private val _lifeList = MutableStateFlow<List<Lifelist>?>(null)
    val lifeList : StateFlow<List<Lifelist>?> = _lifeList.asStateFlow()

    private val _lifeListState = MutableStateFlow(LifeListState())
    val lifeListState: StateFlow<LifeListState> = _lifeListState.asStateFlow()
    fun getUserLifeList(){
        viewModelScope.launch {
            _lifeListState.value = _lifeListState.value.copy(isLoading = true)
            try {
                val listItems = lifelistRepository.getAllItemsInLifeList()
                _lifeList.value = listItems
                _lifeListState.value = LifeListState(lifeList = listItems, isLoading = false)

            }catch (e: Exception){
                Log.e("getUserLifeList", "Error retrieving the life list: ${e.message}")
                _lifeListState.value = LifeListState(isLoading = false, isFailure = true)

            }
        }
    }

    private val _sightingsFlow = MutableStateFlow<List<FloraFaunaSighting>>(emptyList())
    val sightingsFlow = _sightingsFlow.asStateFlow()

    fun getSightingsNearLocation(geoPoint: GeoPoint, radiusInKm: Double, limit: Int) {
        viewModelScope.launch {
            Log.d("FloraFauna", "Getting trips near user location: $geoPoint")
            when (val result = lifelistRepository.getSightingsNearLocation(geoPoint, radiusInKm, limit)) {
                is OperationResult.Success -> {


                    _sightingsFlow.value = result.data
                    Log.d("FloraFauna", "Fetched ${result.data.size} species")
                }
                is OperationResult.Error -> {
                    Log.e("FlorFauna", "Error fetching hikes: ${result.exception}")

                }

                else -> {}
            }
        }
    }


    // Function to clear all data relating to create sighting.
    fun clearSighting() {
        _floraFaunaState.update { currentState ->
            currentState.copy(
                sightingDate = Date(),
                sightingLocation = Location(DefaultLocation.OSLO.lat, DefaultLocation.OSLO.lon)
            )
        }
    }
}