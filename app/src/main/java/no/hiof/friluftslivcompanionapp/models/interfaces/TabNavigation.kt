package no.hiof.friluftslivcompanionapp.models.interfaces

import kotlinx.coroutines.flow.StateFlow
import no.hiof.friluftslivcompanionapp.data.states.TabsUiState
import no.hiof.friluftslivcompanionapp.models.enums.Screen

interface TabNavigation {

    var tabDestinations : Map<Screen, Int>
    val uiState: StateFlow<TabsUiState>
    fun changeHighlightedTab(index: Int)
    fun getHighlightedTab(): Int
}