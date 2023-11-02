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

    suspend fun updateUserDarkModePreference(isDarkMode: Boolean) {
        try {
            val userPreferencesMap = mapOf("isDarkMode" to isDarkMode)
            firestore.collection("users").document(userId)
                .collection("userPreferences").document("preferences")
                .set(userPreferencesMap, SetOptions.merge())
            Log.d(TAG, "Dark mode preference updated successfully: $isDarkMode")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating dark mode preference", e)
        }
    }

    suspend fun updateUserPreferences(userid: String, updatedPreferences: UserPreferences) {
        val currentUser = auth.currentUser ?: return // If there's no user logged in, just return.

        val userPreferencesDocument = firestore
            .collection("users")
            .document(currentUser.uid)
            .collection("preferences")
            .document("userPreferences")

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userPreferencesDocument)

            // If the document already exists, we update it. Otherwise, we create a new one.
            if (snapshot.exists()) {
                transaction.update(userPreferencesDocument, updatedPreferences.toMap())
            } else {
                transaction.set(userPreferencesDocument, updatedPreferences.toMap())
            }
        }.await()
    }

    // Function to get the user preferences from Firestore with real-time updates
    fun getUserPreferencesFlow(): Flow<UserPreferences?> {
        val currentUser = auth.currentUser ?: return flowOf(null) // If no user, return a flow of null.

        val userPreferencesDocument = firestore
            .collection("users")
            .document(currentUser.uid)
            .collection("preferences")
            .document("userPreferences")

        return callbackFlow {
            val listener = userPreferencesDocument.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle any errors appropriately in your app
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    try {
                        val userPreferences = UserPreferences.fromMap(snapshot.data ?: emptyMap())
                        trySend(userPreferences).isSuccess
                    } catch (e: Exception) {
                        // Handle the error if the conversion fails
                        close(e)
                    }
                } else {
                    trySend(null).isSuccess
                }
            }

            // This will be invoked when the flow collector is cancelled
            // which we interpret as when it's no longer needed.
            awaitClose { listener.remove() }
        }
    }
}


