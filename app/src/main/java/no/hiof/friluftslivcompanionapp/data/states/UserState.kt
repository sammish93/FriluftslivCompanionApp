package no.hiof.friluftslivcompanionapp.data.states

import android.location.Location
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import com.google.firebase.auth.FirebaseUser
import no.hiof.friluftslivcompanionapp.models.User
import no.hiof.friluftslivcompanionapp.models.enums.DisplayPicture
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import java.util.Locale

/**
 * Represents the state of the logged in user, primarily focusing on the user's last known location.
 *
 * @property lastKnownLocation The last known [Location] of the user.
 *
 */
data class UserState(
    val currentUser: FirebaseUser? = null,
    val language: SupportedLanguage = SupportedLanguage.ENGLISH,
    val isDarkMode: Boolean = false,
    val displayPicture: DisplayPicture = DisplayPicture.DP_DEFAULT,
    val lastKnownLocation: Location? = null,
    val isLocationManagerCalled: Boolean = false,
    val isLocationPermissionGranted: Boolean = false,
    val isInitiallyNavigatedTo: Boolean = false,
    val isLocationSearchUpdating: Boolean = false,
    val windowSizeClass: WindowSizeClass? = null
)
