package no.hiof.friluftslivcompanionapp.models.interfaces

import kotlinx.coroutines.flow.MutableStateFlow
import no.hiof.friluftslivcompanionapp.models.enums.Screen

interface TabNavigation {

    var tabDestinations : Map<Screen, String>
    var highlightedTab : MutableStateFlow<Int>
    fun changeHighlightedTab(index: Int)
}