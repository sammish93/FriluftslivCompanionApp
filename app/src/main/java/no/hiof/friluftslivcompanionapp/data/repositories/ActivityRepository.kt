package no.hiof.friluftslivcompanionapp.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.Hike
import java.time.LocalDate
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun saveTripActivity(tripActivity: Map<LocalDate, Hike>, tripId: String) {
        val tripActivityCollection = firestore.collection("trips").document(tripId).collection("activities")


        val tripActivityMap = mapOf(
            "activity" to tripActivity
        )

        try {

            val documentReference = tripActivityCollection.add(tripActivityMap).await()
            println("Trip Activity saved with ID: ${documentReference.id}")
        } catch (e: Exception) {
            println("Error saving Trip Activity: $e")
        }
    }
}