package no.hiof.friluftslivcompanionapp.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.hiof.friluftslivcompanionapp.data.states.TabsUiState
import no.hiof.friluftslivcompanionapp.data.states.TripsState
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import no.hiof.friluftslivcompanionapp.models.interfaces.TabNavigation
import java.time.Duration
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
class TripsViewModel @Inject constructor(
    // Communication with the data layer can be injected as dependencies here.
    // private val repository: TripsRepository
) : ViewModel(), TabNavigation {

    private val _uiState = MutableStateFlow(TabsUiState())
    override val uiState: StateFlow<TabsUiState> = _uiState.asStateFlow()

    private val _tripsState = MutableStateFlow(TripsState())
    val tripsState: StateFlow<TripsState> = _tripsState.asStateFlow()

    // State related to google map nodes.
    private val _nodes = MutableStateFlow<List<LatLng>>(listOf())
    val nodes: StateFlow<List<LatLng>> = _nodes.asStateFlow()

    override var tabDestinations = mapOf(
        Screen.TRIPS to Screen.TRIPS.navBarLabel,
        Screen.TRIPS_RECENT_ACTIVITY to Screen.TRIPS_RECENT_ACTIVITY.navBarLabel,
        Screen.TRIPS_CREATE to Screen.TRIPS_CREATE.navBarLabel
    )

    var tripTypes = listOf(
        TripType.HIKE,
        TripType.SKI,
        TripType.CLIMB
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

    // Function to add a node.
    fun addNode(node: LatLng) {
        _nodes.value = _nodes.value + node
        calculateDistanceKmBetweenTwoNodes()
    }

    // Function to remove a node.
    fun removeNode(node: LatLng) {
        _nodes.value = _nodes.value.filter { it != node }
    }

    fun removeNodes() {
        _nodes.value = listOf()
    }

    fun updateIsInitiallyNavigatedTo(isInitiallyNavigatedTo: Boolean) {
        _tripsState.update { currentState ->
            currentState.copy(
                isInitiallyNavigatedTo = isInitiallyNavigatedTo
            )
        }
    }

    fun updateCreateTripType(tripType: TripType) {
        _tripsState.update { currentState ->
            currentState.copy(
                createTripType = tripType
            )
        }
    }

    fun updateCreateTripDuration(duration: Duration) {
        if (!duration.isNegative) {
            _tripsState.update { currentState ->
                currentState.copy(
                    createTripDuration = duration
                )
            }
        }
    }

    fun updateCreateTripDifficulty(difficulty: Int) {
        if (difficulty in 1..5) {
            _tripsState.update { currentState ->
                currentState.copy(
                    createTripDifficulty = difficulty
                )
            }
        }
    }

    fun updateCreateTripDescription(description: String) {
        _tripsState.update { currentState ->
            currentState.copy(
                createTripDescription = description
            )
        }
    }

    private fun incrementCreateTripDistanceKm(distanceKm: Double) {
        val previousDistance = _tripsState.value.createTripDistanceKm

        _tripsState.update { currentState ->
            currentState.copy(
                createTripDistanceKm = previousDistance + distanceKm
            )
        }
    }

    private fun calculateDistanceKmBetweenTwoNodes() {
        //TODO calculate between long and lat
        incrementCreateTripDistanceKm(1.0)
    }

    // Function to clear all data relating to create trip.
    fun clearTrip() {
        _tripsState.update { currentState ->
            currentState.copy(
                createTripType = TripType.HIKE,
                createTripDuration = Duration.ofHours(1).plus(Duration.ofMinutes(30)),
                createTripDifficulty = 3,
                createTripDescription = "",
                createTripDistanceKm = 1.0
            )
        }
        removeNodes()
    }

    fun createTrip() {
        val tripState = _tripsState.value
        val route = _nodes.value
        val trip = TripFactory.createTrip(
            tripState.createTripType,
            route,
            tripState.createTripDescription,
            tripState.createTripDuration,
            tripState.createTripDistanceKm,
            tripState.createTripDifficulty
        )
    }
}