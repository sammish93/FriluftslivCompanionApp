package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.UserPreferences
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

            OperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }

}


