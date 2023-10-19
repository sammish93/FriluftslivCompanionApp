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
     * the firestore auth token. It can also be used for editing an
     * existing user's fields
     */
    suspend fun createUser(user: User): OperationResult<Unit> {
        return try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid

                val userCollection = firestore.collection("users")
                val userDocument = userCollection.document(userId)

                // Prepare the user data map
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


                OperationResult.Success(Unit)
            } else {

                OperationResult.Error(IllegalStateException("No authenticated user found."))
            }
        } catch (e: Exception) {

            e.printStackTrace()
            OperationResult.Error(e)
        }
    }


    suspend fun getUser(uid: String): OperationResult<User> {
        return try {
            val userCollection = firestore.collection("users")
            val userDocument = userCollection.document(uid)

            val documentSnapshot = userDocument.get().await()

            if (documentSnapshot.exists()) {

                val userData = documentSnapshot.toObject(User::class.java)
                if (userData != null) {
                    OperationResult.Success(userData.copy(userId = documentSnapshot.id))
                } else {

                    OperationResult.Error(Exception("Failed to convert document to User object."))
                }
            } else {

                OperationResult.Error(Exception("No User found with the provided UID."))
            }
        } catch (e: Exception) {

            e.printStackTrace()
            OperationResult.Error(e)
        }
    }


    suspend fun deleteUser(uid: String): OperationResult<Unit> {
        return try {
            val userCollection = firestore.collection("users")
            val userDocument = userCollection.document(uid)

            val documentSnapshot = userDocument.get().await()

            if (documentSnapshot.exists()) {

                userDocument.delete().await()

                OperationResult.Success(Unit)
            } else {

                OperationResult.Error(Exception("No User found with the provided UID."))
            }
        } catch (e: Exception) {

            e.printStackTrace()
            OperationResult.Error(e)
        }
    }



}