package no.hiof.friluftslivcompanionapp.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.data.repositories.ActivityRepository
import no.hiof.friluftslivcompanionapp.data.repositories.OperationResult
import no.hiof.friluftslivcompanionapp.data.repositories.TripsRepository
import no.hiof.friluftslivcompanionapp.data.states.TabsUiState
import no.hiof.friluftslivcompanionapp.data.states.TripsState
import no.hiof.friluftslivcompanionapp.domain.FloraFaunaFactory
import no.hiof.friluftslivcompanionapp.domain.LocationFormatter
import no.hiof.friluftslivcompanionapp.domain.TripFactory
import no.hiof.friluftslivcompanionapp.models.DummyTrip
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import no.hiof.friluftslivcompanionapp.models.interfaces.TabNavigation
import java.time.Duration
import java.time.LocalDate
import java.util.Date
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
    private val tripsRepository: TripsRepository,
    private val activityRepository : ActivityRepository
) : ViewModel(), TabNavigation {

    private val _uiState = MutableStateFlow(TabsUiState())
    override val uiState: StateFlow<TabsUiState> = _uiState.asStateFlow()

    private val _tripsState = MutableStateFlow(TripsState())
    val tripsState: StateFlow<TripsState> = _tripsState.asStateFlow()

    // State related to google map nodes.
    private val _nodes = MutableStateFlow<List<LatLng>>(listOf())
    val nodes: StateFlow<List<LatLng>> = _nodes.asStateFlow()

    // State related to trips from the database.
    private val _dbHikes = MutableStateFlow<List<Hike>>(emptyList())
    val hikes: StateFlow<List<Hike>> = _dbHikes.asStateFlow()

    // Used to show error message related to hike fetches.
    val errorMessage: MutableState<String?> = mutableStateOf(null)

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

    // Used to get trips from the db which is near the users location.
    fun getTripsNearUsersLocation(geoPoint: GeoPoint, radiusInKm: Double, limit: Int) {
        viewModelScope.launch {
            Log.d("TripsViewModel", "Getting trips near user location: $geoPoint")
            when (val result = tripsRepository.getTripsNearUsersLocation(geoPoint, radiusInKm, limit)) {
                is OperationResult.Success -> {
                    Log.d("TripsViewModel", "Fetched ${result.data.size} hikes")
                    _dbHikes.value = result.data
                }
                is OperationResult.Error -> {
                    Log.e("TripsViewModel", "Error fetching hikes: ${result.exception}")
                    errorMessage.value = "There are currently no trips near your location."
                }
            }
        }
    }

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

    private fun updateCreateTripDistanceKm(distanceMeters: Double) {
        _tripsState.update { currentState ->
            currentState.copy(
                createTripDistanceKm = distanceMeters
            )
        }
    }

    // Function to clear all data relating to create trip.
    fun clearTrip() {
        _tripsState.update { currentState ->
            currentState.copy(
                createTripType = TripType.HIKE,
                createTripDuration = Duration.ofHours(1).plus(Duration.ofMinutes(30)),
                createTripDifficulty = 3,
                createTripDescription = "",
                createTripDistanceKm = 0.0
            )
        }
        removeNodes()
    }

    // Creates a trip based on state values and then writes to the database.
    fun createTrip() {
        val route = _nodes.value

        updateCreateTripDistanceKm(LocationFormatter.calculateTotalDistanceKilometers(route))

        val tripState = _tripsState.value

        val trip = TripFactory.createTrip(
            tripState.createTripType,
            route,
            tripState.createTripDescription,
            tripState.createTripDuration,
            tripState.createTripDistanceKm,
            tripState.createTripDifficulty
        )

        //TODO Integrate database writing and clear create trip values on successful write.

        if (trip != null) {
            viewModelScope.launch {
                when (val result = tripsRepository.createTrip(trip)) {
                    is OperationResult.Success -> {

                        clearTrip()
                    }
                    is OperationResult.Error -> {

                        val exception = result.exception
                        Log.e(TAG, "Error writing trip to Firestore: ${exception.message}")
                    }
                }
            }
        } else {

        }
    }

    // Updates a Date value to be used when adding a tripActivity.
    fun updateTripActivityDate(date: Date) {
        _tripsState.update { currentState ->
            currentState.copy(
                tripActivityDate = date
            )
        }
    }

    // Creates a TripActivity object based on state values and then writes to the database.
    fun createTripActivity() {
        val tripsState = _tripsState.value

        val tripActivity = tripsState.selectedTrip?.let {
            TripFactory.createTripActivity(
                it,
                tripsState.tripActivityDate
            )
        }

        //TODO Integrate database writing and clear create tripActivity values on successful write.

        /*
        if (sighting != null) {
            viewModelScope.launch {
                when (val result = tripsRepository.createTrip(trip)) {
                    is OperationResult.Success -> {

                        clearTrip()
                    }
                    is OperationResult.Error -> {

                        val exception = result.exception
                        Log.e(TripsViewModel.TAG, "Error writing trip to Firestore: ${exception.message}")
                    }
                }
            }
        } else {

        }
         */

        clearTripActivity()
    }

    // Function to clear all data relating to create tripActivity.
    fun clearTripActivity() {
        _tripsState.update { currentState ->
            currentState.copy(
                tripActivityDate = Date()
            )
        }
    }

    private val _recentActivity = MutableStateFlow<List<TripActivity>?>(null)
    val recentActivity: StateFlow<List<TripActivity>?> = _recentActivity
    fun recentActivity(){
        viewModelScope.launch{
            try{
                when (val result = activityRepository.getUserTripActivities()){
                    is OperationResult.Success -> _recentActivity.value = result.data
                    is OperationResult.Error -> {

                        Log.e(TAG, "Error fetching recent activity: ${result.exception.message}")

                    }
                }

            }catch (e: Exception){
                Log.e(TAG, "Exception fetching recent activities: ${e.message}")
            }
        }

    }
    companion object {
        private const val TAG = "TripsViewModel"
    }

    fun updateSelectedTrip(trip: Trip) {
        _tripsState.update { currentState ->
            currentState.copy(
                selectedTrip = trip
            )
        }
    }

    fun updateSelectedTripDate(tripDate: LocalDate) {
        _tripsState.update { currentState ->
            currentState.copy(
                selectedTripDate = tripDate
            )
        }
    }

    fun updateSelectedTripIsRecentActivity(isRecentActivity: Boolean) {
        _tripsState.update { currentState ->
            currentState.copy(
                isSelectedTripRecentActivity = isRecentActivity
            )
        }
    }
}