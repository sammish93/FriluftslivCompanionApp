package no.hiof.friluftslivcompanionapp.data.states

import android.location.Location
import com.google.firebase.auth.FirebaseUser

/**
 * Represents the state of the logged in user, primarily focusing on the user's last known location.
 *
 * @property lastKnownLocation The last known [Location] of the user.
 *
 */
data class UserState(
    val currentUser: FirebaseUser? = null,
    val lastKnownLocation: Location? = null,
    val isLocationManagerCalled: Boolean = false,
    val isLocationPermissionGranted: Boolean = false,
    val isInitiallyNavigatedTo: Boolean = false
)
