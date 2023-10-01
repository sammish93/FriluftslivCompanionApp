package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.Lifelist
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.User
import no.hiof.friluftslivcompanionapp.models.UserPreferences
import javax.inject.Inject
// Responsible for retrieving data from the db about a specific user.
// NOTE: also uses ActivityRepository, LifelistRepository, and PreferencesRepository
// to retrieve a single user's activity, lifelist, and user preferences.
// The instantiation of said user object would be best suited to the domain layer.
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    /*
   fun createUser(user: no.hiof.friluftslivcompanionapp.models.User, onComplete: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            // Set the user document in Firestore with the Firebase Auth UID as the document ID
            firestore.collection("users")
                .document(userId)
                .set(user, SetOptions.merge())
                .addOnSuccessListener {
                    onComplete(true)
                }
                .addOnFailureListener {
                    onComplete(false)
                }
        } else {
            onComplete(false)
        }
    }


     */

    suspend fun createUser(user: User) {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid


                val userCollection = firestore.collection("users")
                val userDocument = userCollection.document(userId)


                userDocument.set(user).await()


            } else {

                throw IllegalStateException("No authenticated user found.")
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }



}