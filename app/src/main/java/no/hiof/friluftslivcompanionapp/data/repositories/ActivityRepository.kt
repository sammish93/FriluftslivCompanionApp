package no.hiof.friluftslivcompanionapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

        //Use this to add an activity to recent activity
    suspend fun addHikeActivityToUser(date: Date, tripId: String): Result<Boolean> = withContext(
        Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid
                ?: return@withContext Result.failure(Exception("No user logged in"))



            val userDocumentRef = firestore.collection("users").document(userId)

            val tripDocumentRef = firestore.collection("trips").document(tripId)
            val tripDocument = tripDocumentRef.get().await()


            if (!tripDocument.exists()) {
                return@withContext Result.failure(Exception("Trip not found"))
            }
            val tripData = tripDocument.data
                ?: return@withContext Result.failure(Exception("Data is missing from the trip"))



            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = dateFormat.format(date)


            val tripActivityUpdate = mapOf(
                "tripActivity.activity.$dateString" to tripData
            )


            userDocumentRef.update(tripActivityUpdate).await()

            Result.success(true)
        } catch (e: Exception) {

            Result.failure(e)
        }
    }



}