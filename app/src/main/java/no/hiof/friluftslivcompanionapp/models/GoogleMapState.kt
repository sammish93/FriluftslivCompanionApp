package no.hiof.friluftslivcompanionapp.models

import android.location.Location

/**
 * Represents the state of the Google Map, primarily focusing on the user's last known location.
 *
 * @property lastKnownLocation The last known [Location] of the user.
 *
 * The class is used to encapsulate the necessary information required to render and interact with
 * the Google Map Composable.
 */
data class GoogleMapState(
    val lastKnownLocation: Location?
)
