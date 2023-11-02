package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.models.UserPreferences
import no.hiof.friluftslivcompanionapp.models.enums.DisplayPicture
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val  auth: FirebaseAuth
) {
    private val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
    companion object {
        private const val TAG = "Preference Repository"
    }

    suspend fun updateUserDarkModePreference(isDarkMode: Boolean): OperationResult<Unit> {
        return try {
            val userDocument = firestore.collection("users").document(userId)

            userDocument.update("preferences.darkModeEnabled", isDarkMode).await()
            Log.d("FirestoreDebug", "Updating dark mode preference to: $isDarkMode")


            OperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }

    suspend fun fetchUserDarkModePreference(): Boolean {
        try {

            val userId = auth.currentUser?.uid
            if (userId == null) {
                Log.d("DebugDarkMode", "User not logged in")
                throw IllegalStateException("User not logged in")
            } else {
                Log.d("DebugDarkMode", "User ID: $userId")
            }


            val document = firestore.collection("users").document(userId).get().await()
            Log.d("DebugDarkMode", "Fetched document for user: $document")


            val userPreferences = document["preferences"] as? Map<String, Any>
            if (userPreferences == null) {
                Log.d("DebugDarkMode", "userPreferences is null")
            } else {
                Log.d("DebugDarkMode", "userPreferences: $userPreferences")
            }


            val isDarkMode = userPreferences?.get("darkModeEnabled") as? Boolean ?: false
            Log.d("DebugDarkMode", "isDarkMode value: $isDarkMode")

            return isDarkMode

        } catch (e: Exception) {

            Log.e("DebugDarkMode", "Error fetching dark mode preference", e)
            throw e
        }
    }

    suspend fun updateUserDisplayPicture(displayPicture: DisplayPicture): OperationResult<Unit> {
        return try {
            val userDocument = firestore.collection("users").document(userId)

            userDocument.update("preferences.displayPicture", displayPicture).await()

            OperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }

    suspend fun fetchUserDisplayPicture(): DisplayPicture {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val document = firestore.collection("users").document(userId).get().await()
        val userPreferences = document["preferences"] as? Map<String, Any>
        val displayPictureString = userPreferences?.get("displayPicture") as? String ?: DisplayPicture.DP_DEFAULT.name
        return DisplayPicture.valueOf(displayPictureString)
    }



}


