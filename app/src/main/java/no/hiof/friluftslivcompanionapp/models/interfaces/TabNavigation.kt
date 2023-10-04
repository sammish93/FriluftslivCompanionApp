package no.hiof.friluftslivcompanionapp.models.interfaces

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import no.hiof.friluftslivcompanionapp.data.TabsUiState
import no.hiof.friluftslivcompanionapp.models.enums.Screen

interface TabNavigation {

    var tabDestinations : Map<Screen, String>
    val uiState: StateFlow<TabsUiState>
    fun changeHighlightedTab(index: Int)
}