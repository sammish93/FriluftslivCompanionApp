package no.hiof.friluftslivcompanionapp.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.data.states.TabsUiState
import no.hiof.friluftslivcompanionapp.data.states.WeatherState
import no.hiof.friluftslivcompanionapp.domain.WeatherDeserialiser
import no.hiof.friluftslivcompanionapp.models.WeatherForecast
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.models.interfaces.TabNavigation
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

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    private val api = WeatherDeserialiser.getInstance()

    suspend fun getWeatherForecast() {
        val result = api.getWeatherForecast(41.389, 2.159)

        if (result is Result.Success) {
            updateWeatherState(result.value)
        } else null
    }

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

    fun updateWeatherState(weatherForecast: WeatherForecast) {
        _weatherState.update { currentState ->
            currentState.copy(
                currentWeather = weatherForecast.forecast[0],
                todayWeather = weatherForecast.forecast[1],
                todayPlusOneWeather = weatherForecast.forecast[2],
                todayPlusTwoWeather = weatherForecast.forecast[3],
                todayPlusThreeWeather = weatherForecast.forecast[4],
                todayPlusFourWeather = weatherForecast.forecast[5],
                todayPlusFiveWeather = weatherForecast.forecast[6],
                todayPlusSixWeather = weatherForecast.forecast[7],
                todayPlusSevenWeather = weatherForecast.forecast[8]
            )
        }
    }
}