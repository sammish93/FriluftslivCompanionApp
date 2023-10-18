package no.hiof.friluftslivcompanionapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.User
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

        //Use this to add an activity to recent activity
    suspend fun addHikeActivityToUser(date: Date, hike: Hike): Result<Boolean> = withContext(
        Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid
                ?: return@withContext Result.failure(Exception("No user logged in"))

            val userDocumentRef = firestore.collection("users").document(userId)

            val hikeData = hikeToMap(hike)

            val tripActivityUpdate = mapOf(
                "activity.$date" to hikeData
            )

            userDocumentRef.update(tripActivityUpdate).await()

            Result.success(true)
        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    private fun hikeToMap(hike: Hike): Map<String, Any?> {
        return mapOf(
            "documentId" to hike.documentId,
            "startLocation" to hike.startLocation,
            "endLocation" to hike.endLocation,
            "description" to hike.description,
            "duration" to hike.duration,
            "distanceKm" to hike.distanceKm,
            "difficulty" to hike.difficulty
        )
    }

}