package no.hiof.friluftslivcompanionapp.data.states

import android.location.Location

/**
 * Represents the state of the Trips pages, primarily focusing on GoogleMaps functionality.
 *
 * @property isInitiallyNavigatedTo Whether the map has called an animation to zoom in to the user's
 * location.
 *
 */
data class TripsState(
    val isInitiallyNavigatedTo: Boolean = false
)
