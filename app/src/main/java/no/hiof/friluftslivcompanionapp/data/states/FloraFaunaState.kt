package no.hiof.friluftslivcompanionapp.data.states

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.BirdInfo
import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.enums.WeatherUnit

data class FloraFaunaState(
    val birdResults: List<Bird> = emptyList(),
    val selectedBirdInfo: BirdInfo? = null,
    val isLoading: Boolean = false
)
