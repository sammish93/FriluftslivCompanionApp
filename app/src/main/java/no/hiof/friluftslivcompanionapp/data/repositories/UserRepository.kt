package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.models.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
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


                    "username" to user.username,
                    "email" to user.email,
                    "preferences" to user.preferences,
                    "yearlyTripCount" to user.yearlyTripCount,
                    "yearlySpeciesCount" to user.yearlySpeciesCount
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

    suspend fun getUser(uid: String): User {
        val userCollection = firestore.collection("users")
        val userDocument = userCollection.document(uid)

        val documentSnapshot = userDocument.get().await()

        if (documentSnapshot.exists()) {
            val userData = documentSnapshot.toObject(User::class.java)
            if (userData != null) {


                return userData.copy(userId = documentSnapshot.id)

            } else {
                throw Exception("Failed to convert document to User object.")
            }
        } else {
            throw Exception("No User found with the provided UID.")
        }
    }

    //Used to update anonymous user account's username and email
    suspend fun updateUser(user: User) = withContext(Dispatchers.IO) {
        try {
            firestore.collection("users").document(user.userId)

                .set(user, SetOptions.merge()).await()

            Log.d("UserRepository", "User successfully updated!")
        } catch (e: Exception) {
            Log.w("UserRepository", "Error updating user", e)
            throw e
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

    suspend fun addYearlyFieldsToAllUsers(): OperationResult<Unit> {
        return try {
            val userCollection = firestore.collection("users")


            val querySnapshot = userCollection.get().await()

            for (document in querySnapshot.documents) {
                val userDocument = userCollection.document(document.id)


                userDocument.update(
                    "yearlyTripCount", 0,
                    "yearlySpeciesCount", 0
                ).await()
            }

            OperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }

    suspend fun getTopThreeUsersByTripCount(): OperationResult<List<User>> {
        return try {
            val userCollection = firestore.collection("users")

            val querySnapshot = userCollection
                .orderBy("yearlyTripCount", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .await()

            val users = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }

            OperationResult.Success(users)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }

    suspend fun getTopThreeUsersBySpeciesCount(): OperationResult<List<User>> {
        return try {
            val userCollection = firestore.collection("users")


            val querySnapshot = userCollection
                .orderBy("yearlySpeciesCount", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .await()

            val users = querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }

            OperationResult.Success(users)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }


    suspend fun updateUserName(uid: String, newUsername: String): OperationResult<Unit> {
        return try {
            val userCollection = firestore.collection("users")
            val userDocument = userCollection.document(uid)

            userDocument.update("username", newUsername).await()

            OperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }
}