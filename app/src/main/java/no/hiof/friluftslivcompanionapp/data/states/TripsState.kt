package no.hiof.friluftslivcompanionapp.data.states

import android.location.Location
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import java.time.Duration

/**
 * Represents the state of the Trips pages, primarily focusing on GoogleMaps functionality.
 *
 * @property isInitiallyNavigatedTo Whether the map has called an animation to zoom in to the user's
 * location.
 *
 */
data class TripsState(
    val isInitiallyNavigatedTo: Boolean = false,
    val createTripType: TripType = TripType.HIKE,
    val createTripDuration: Duration = Duration.ofHours(1).plus(Duration.ofMinutes(30)),
    val createTripDifficulty: Int = 3,
    val createTripDescription: String = "",
    val createTripDistanceKm: Double = 0.0
)
