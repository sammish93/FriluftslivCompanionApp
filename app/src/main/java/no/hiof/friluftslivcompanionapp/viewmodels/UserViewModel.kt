package no.hiof.friluftslivcompanionapp.viewmodels

import android.location.Location
import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.data.api.PlacesApi
import no.hiof.friluftslivcompanionapp.data.repositories.ActivityRepository
import no.hiof.friluftslivcompanionapp.data.repositories.LifelistRepository
import no.hiof.friluftslivcompanionapp.data.repositories.OperationResult
import no.hiof.friluftslivcompanionapp.data.repositories.PreferencesRepository
import no.hiof.friluftslivcompanionapp.data.repositories.UserRepository
import no.hiof.friluftslivcompanionapp.data.states.AutoCompleteState
import no.hiof.friluftslivcompanionapp.data.states.PlaceInfoState
import no.hiof.friluftslivcompanionapp.data.states.UserState
import no.hiof.friluftslivcompanionapp.domain.LocationFormatter
import no.hiof.friluftslivcompanionapp.models.User
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.models.enums.DisplayPicture
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import javax.inject.Inject

/**
 * A ViewModel responsible for managing and updating the state of the Google Map.
 *
 * This ViewModel holds the state of the map, specifically the last known location of the user.
 * It provides functions to update the map's state based on changes in the user's location.
 *
 * @property state Represents the current state of the Google Map, encapsulated in a [UserState] object.
 * The state can be observed and updated as needed.
 *
 * @constructor Creates a new instance of the MapViewModel. The initial state sets the last known location to null.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val placesApi: PlacesApi,
    private val placesClient: PlacesClient,
    private val activityRepository: ActivityRepository,
    private val lifeListRepository: LifelistRepository,
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserState(lastKnownLocation = null))
    val state: StateFlow<UserState> = _state.asStateFlow()

    // Used for Places API.
    val locationAutoFill = mutableStateListOf<AutoCompleteState>()

    private val _placeInfoState = MutableStateFlow<PlaceInfoState?>(null)
    val placeInfoState: StateFlow<PlaceInfoState?> = _placeInfoState

    var supportedLanguages = listOf(
        SupportedLanguage.ENGLISH,
        SupportedLanguage.NORWEGIAN
    )

    var displayPictures = listOf(
        DisplayPicture.DP_DEFAULT,
        DisplayPicture.DP_ONE,
        DisplayPicture.DP_TWO,
        DisplayPicture.DP_THREE,
        DisplayPicture.DP_FOUR,
        DisplayPicture.DP_FIVE,
        DisplayPicture.DP_SIX,
        DisplayPicture.DP_SEVEN,
        DisplayPicture.DP_EIGHT,
    )

    // Used to get city, county, country and coordinates.
    fun fetchPlaceInfo(placeId: String) {
        viewModelScope.launch {
            updateIsLocationSearchUpdating(true)
            try {
                val placeInfo = placesApi.fetchPlaceInfo(placeId)
                _placeInfoState.value = placeInfo

                // Just for testing.
                logPlaceInformation(placeInfoState)

            } catch (e: Exception) {
                Log.i("PlaceInfo", "Could not find the place: ${e.message}")
            }
            updateIsLocationSearchUpdating(false)
        }
    }

    private val _tripCountForTheYear = MutableStateFlow<Int?>(null)
    val tripCountForTheYear: StateFlow<Int?> get() = _tripCountForTheYear

    fun fetchTripCountForTheYear() {
        viewModelScope.launch {
            try {
                val activitiesResult = activityRepository.getUserTripCountForTheYear()
                if (activitiesResult is OperationResult.Success) {
                    _tripCountForTheYear.value = activitiesResult.data
                } else {
                    _tripCountForTheYear.value = null
                }
            } catch (e: Exception) {
                Log.e("TripCount", "Error fetching trip count: ${e.message}", e)
            }
        }
    }

    private val _totalKilometers = MutableStateFlow("")
    val totalKilometers: StateFlow<String> = _totalKilometers.asStateFlow()

    fun fetchTotalKilometersForTheYear() {
        viewModelScope.launch {
            val totalKm = activityRepository.getTotalKilometersForYear()
            _totalKilometers.value = roundToOneDecimalPlace(totalKm)
        }
    }


    private val _speciesCount = MutableStateFlow<Int?>(null)
    val speciesCount: StateFlow<Int?> get() = _speciesCount

    fun fetchSpeciesCountForThisYear() {
        viewModelScope.launch {
            _speciesCount.value = lifeListRepository.countSpeciesSightedThisYear()
        }
    }

    private val _topThreeUsersByTripCount = MutableStateFlow<List<User>?>(null)
    val topThreeUsersByTripCount: StateFlow<List<User>?> get() = _topThreeUsersByTripCount

    fun fetchTopThreeUsersByTripCount() {
        Log.d("UserTripCount", "Fetching top three users by trip count")
        viewModelScope.launch {
            val result = userRepository.getTopThreeUsersByTripCount()
            if (result is OperationResult.Success) {
                _topThreeUsersByTripCount.value = result.data
                Log.i("UserTripCount", "Successfully fetched top three users")
            } else {
                _topThreeUsersByTripCount.value = null
                Log.e("UserTripCount", "Failed to fetch top three users")
            }
        }
        Log.d("UserTripCount", "Finished fetching top three users by trip count")
    }

    private val _topThreeUsersBySpeciesCount = MutableStateFlow<List<User>?>(null)
    val topThreeUsersBySpeciesCount: StateFlow<List<User>?> get() = _topThreeUsersBySpeciesCount

    fun fetchTopThreeUsersBySpeciesCount() {
        Log.d("Profile:UserSpeciesCount", "Fetching top three users by species count")
        viewModelScope.launch {
            val result = userRepository.getTopThreeUsersBySpeciesCount()
            if (result is OperationResult.Success) {
                _topThreeUsersBySpeciesCount.value = result.data
                Log.i("Profile:UserSpeciesCount", "Successfully fetched top three users by species count")
            } else {
                _topThreeUsersBySpeciesCount.value = null
                Log.e("Profile:UserSpeciesCount", "Failed to fetch top three users by species count")
            }
        }
        Log.d("Profile:UserSpeciesCount", "Finished fetching top three users by species count")
    }






    // Updates the last known location in the map's state.
    fun updateLocation(location: Location?) {
        _state.update { currentState ->
            currentState.copy(
                lastKnownLocation = location
            )
        }
    }


    // Updates the currently logged in user.
    fun updateCurrentUser(currentUser: FirebaseUser) {
        _state.update { currentState ->
            currentState.copy(
                currentUser = currentUser
            )
        }
    }

    // Returns the current user as a FirebaseUser object.
    fun getCurrentUser(): FirebaseUser? {
        return _state.value.currentUser
    }

    // Updates whether dark mode is enabled.
    //TODO Write this value to a firebase user.
    fun updateDarkMode(isDarkMode: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isDarkMode = isDarkMode
            )
        }

        viewModelScope.launch {
            try {
                preferencesRepository.updateUserDarkModePreference(isDarkMode)
                Log.i("Profile:DarkModeUpdate", "Dark mode preference successfully updated:$isDarkMode")
            } catch (e: Exception) {
                Log.e("Profile:DarkModeUpdate", "Error updating dark mode preference, Exception: ${e.message}")

                _state.update { currentState ->
                    currentState.copy(

                    )
                }
            }
        }
    }

    //exposes the isDarkMode from the current state
    val isDarkMode: StateFlow<Boolean> = _state.map { it.isDarkMode }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _state.value.isDarkMode
    )

    fun fetchDarkModePreference(userId: String) {
        viewModelScope.launch {
            try {
                val isDarkModeFromDb = preferencesRepository.fetchUserDarkModePreference()
                updateDarkMode(isDarkModeFromDb)
                Log.i("DarkModePref", "Successfully fetched and updated dark mode preference for user ID: $userId")

            } catch (e: Exception) {
                // Handle exception
                Log.e("Profile:DarkModePreference",
                    "Error fetching dark mode preference for user ID: $userId, Exception: ${e.message}")

            }
        }
    }

    // Updates whether dark mode is enabled.
    fun getIsDarkMode() : Boolean {
        return _state.value.isDarkMode
    }

    // Updates the user's chosen display picture.
    //TODO Write this value to a firebase user.
    fun updateDisplayPicture(displayPicture: DisplayPicture) {
        _state.update { currentState ->
            currentState.copy(
                displayPicture = displayPicture
            )
        }

        viewModelScope.launch {
            try {
                preferencesRepository.updateUserDisplayPicture(displayPicture)
                Log.i("Profile: DisplayPictureUpdate", "Display picture successfully updated")

            } catch (e: Exception) {
                // Handle exception
                Log.e("Profile: DisplayPictureUpdate", "Error updating display picture, Exception: ${e.message}")

            }
        }
    }

    fun fetchDisplayPicture(userId: String) {
        viewModelScope.launch {
            try {
                val displayPictureFromDb = preferencesRepository.fetchUserDisplayPicture()
                updateDisplayPicture(displayPictureFromDb)
            } catch (e: Exception) {
                // Handle exception
                Log.e("Profile:DisplayPicture", "Error fetching display picture for user ID: $userId, Exception: ${e.message}")
            }
        }
    }


    // Retrieves the logged in user's current display picture.
    fun getDisplayPicture() : DisplayPicture {
        return _state.value.displayPicture
    }

    // Updates the status of the location manager loading a user's GPS position.
    fun updateLocationManagerCalled(isLocationManagerCalled: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isLocationManagerCalled = isLocationManagerCalled
            )
        }
    }

    // Updates whether the user has shared their location.
    fun updateLocationPermissionGranted(isLocationPermissionGranted: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isLocationPermissionGranted = isLocationPermissionGranted
            )
        }
    }

    // Updates the user's preferred application language.
    fun updateLanguage(language: SupportedLanguage) {
        _state.update { currentState ->
            currentState.copy(
                language = language
            )
        }

        viewModelScope.launch {
            try {
                preferencesRepository.updateUserLanguage(language)
                Log.i("Profile:LanguageUpdate", "Language successfully updated to: $language")

            }catch (e:Exception){
                Log.e("Profile:LanguageUpdate", "Error updating language, Exception: ${e.message}")
            }
        }
    }

    fun fetchUserLanguagePreference(userId: String){
        viewModelScope.launch {
            try {
                val supportedLanguageFromDb = preferencesRepository.fetchUserSupportedLanguage()
                updateLanguage(supportedLanguageFromDb)
                Log.i("Profile:LanguagePreference", "Successfully updated language preference for user ID: $userId")

            }catch (e: Exception){
                Log.e("Profile:LanguagePreference", "Error fetching language preference for user ID: $userId, Exception: ${e.message}")
            }
        }
    }

    fun getLanguage(): SupportedLanguage {
        return  _state.value.language
    }

    // FUNCTIONS USED FOR PLACES API.
    fun searchPlaces(query: String, countryLimit: String = "") {

        // Empty old result.
        locationAutoFill.clear()

        val request = getAutocompleteRequester(query, countryLimit)
        handleAutocompletePrediction(request)
    }

    fun clearAutocompleteResults() {
        locationAutoFill.clear()
    }

    private fun getAutocompleteRequester(query: String, countryLimit: String = ""): FindAutocompletePredictionsRequest {
        val bounds = LocationFormatter.createRectangularBoundsFromLatLng(
            _state.value.lastKnownLocation?.latitude ?: DefaultLocation.OSLO.lat,
            _state.value.lastKnownLocation?.longitude ?: DefaultLocation.OSLO.lon
        )

        return FindAutocompletePredictionsRequest.builder()
            .setLocationBias(bounds)
            .setQuery(query)
            .setCountries(countryLimit)
            .build()
    }

    private fun handleAutocompletePrediction(request: FindAutocompletePredictionsRequest) {
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            locationAutoFill.addAll(
                response.autocompletePredictions.map {
                    AutoCompleteState(
                        it.getFullText(null).toString(),
                        it.placeId
                    )
                }
            )
        }
    }

    private fun logPlaceInformation(info: StateFlow<PlaceInfoState?>) {
        Log.i("PlaceInfo", "City: ${info.value?.city}")
        Log.i("PlaceInfo", "County: ${info.value?.county}")
        Log.i("PlaceInfo", "Country: ${info.value?.country}")
        Log.i("PlaceInfo", "Coordinates: ${info.value?.coordinates}")
    }

    fun roundToOneDecimalPlace(value: Double): String {
        return String.format("%.1f", value)
    }


    // Updates the last known location in the map's state.
    fun updateIsLocationSearchUpdating(isLocationSearchUpdating: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isLocationSearchUpdating = isLocationSearchUpdating
            )
        }
    }

    // Updates the value of the device's window size.
    fun updateWindowSizeClass(windowSizeClass: WindowSizeClass) {
        _state.update { currentState ->
            currentState.copy(
                windowSizeClass = windowSizeClass
            )
        }
    }

    // Updates whether or not the rail bar has been opened.
    fun updateIsRailBarOpened(isRailBarOpened: Boolean) {
        _state.update { currentState ->
            currentState.copy(
                isRailBarOpened = isRailBarOpened
            )
        }
    }

    // Updates whether or not the drawer bar has been opened.
    fun updateIsDrawerBarOpened(drawerValueState: DrawerValue) {
        _state.update { currentState ->
            currentState.copy(
                isDrawerBarOpened = drawerValueState
            )
        }
    }
}