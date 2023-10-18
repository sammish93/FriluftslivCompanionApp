package no.hiof.friluftslivcompanionapp.data.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.Lifelist
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.User
import no.hiof.friluftslivcompanionapp.models.UserPreferences
import javax.inject.Inject
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    /**
     * Method for creating a new user with the id of
     * the firestire auth token. It can also be used for editing an
     * existing user's fields
     */
    suspend fun createUser(user: User) {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid

                val userCollection = firestore.collection("users")
                val userDocument = userCollection.document(userId)


                val userData = hashMapOf<String, Any?>(
                    "userId" to user.userId,
                    "username" to user.username,
                    "email" to user.email,
                    "preferences" to user.preferences

                )


                user.lifelist?.let {
                    userData["lifelist"] = it
                }
                user.tripActivity?.let {
                    userData["tripActivity"] = it
                }


                userDocument.set(userData, SetOptions.merge()).await()

            } else {
                throw IllegalStateException("No authenticated user found.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO logging messages
        }
    }

    suspend fun getUser(uid: String): User? {
        return try {
            val userCollection = firestore.collection("users")
            val userDocument = userCollection.document(uid)

            val documentSnapshot: DocumentSnapshot = userDocument.get().await()

            if (documentSnapshot.exists()) {
                val userData = documentSnapshot.toObject(User::class.java)
                userData?.copy(userId = documentSnapshot.id)
            } else {

                null
            }
        } catch (e: Exception) {

            e.printStackTrace()
            null
        }
    }

    suspend fun deleteUser(uid: String): Boolean {
        return try {
            val userCollection = firestore.collection("users")
            val userDocument = userCollection.document(uid)

            val documentSnapshot: DocumentSnapshot = userDocument.get().await()

            if (documentSnapshot.exists()) {

                userDocument.delete().await()
                true
            } else {

                false
            }
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "Firestore Exception: ${e.message}", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "An unexpected error occurred: ${e.message}", e)
            false
        }
    }


}