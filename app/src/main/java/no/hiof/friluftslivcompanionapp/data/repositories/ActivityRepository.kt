package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.models.TripActivity
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun addTripActivityToUser(tripId: String, selectedDate: Date) {
        val functionTag = "AddTripActivity"
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            Log.d(functionTag, "User is logged in with ID: $userId")


            val tripDocumentRef = firestore.collection("trips").document(tripId)


            val tripDocument = tripDocumentRef.get().await()

            if (!tripDocument.exists()) {
                Log.w(functionTag, "Trip document does not exist")
                throw IllegalStateException("Trip not found")
            }

            Log.d(functionTag, "Trip document retrieved successfully")


            val tripData = tripDocument.data ?: throw IllegalStateException("Data is missing from the trip")


            val trip = Hike.fromMap(tripData)


            val tripActivity = TripActivity(trip, selectedDate)


            Log.d(functionTag, "Creating a new TripActivity")


            val userTripActivityRef = firestore.collection("users").document(userId).collection("tripActivity")


            userTripActivityRef.add(tripActivity.toMap()).await()

            incrementYearlyTripCount(userId)

            Log.d(functionTag, "TripActivity added successfully to Firestore for user: $userId")
        } catch (e: Exception) {
            Log.e(functionTag, "Error adding trip activity: ${e.message}", e)
            throw e
        }
    }

    private suspend fun incrementYearlyTripCount(uid: String): OperationResult<Unit> {
        return try {
            val userCollection = firestore.collection("users")
            val userDocument = userCollection.document(uid)


            userDocument.update("yearlyTripCount", FieldValue.increment(1)).await()

            OperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }

    suspend fun getUserTripActivities(): List<TripActivity> {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

        return withContext(Dispatchers.IO) {
            try {
                val activityCollectionRef = firestore.collection("users").document(userId).collection("tripActivity")
                val querySnapshot = activityCollectionRef.get().await()

                val tripActivities: MutableList<TripActivity> = mutableListOf()

                for (document in querySnapshot.documents) {
                    val dateTimestamp = document.data?.get("date") as? com.google.firebase.Timestamp
                    val date = dateTimestamp?.toDate()

                    val tripMap = document.data?.get("trip") as? Map<String, Any?>
                    if (tripMap == null || date == null) {
                        continue
                    }

                    val trip = Hike.fromMap(tripMap)
                    tripActivities.add(TripActivity(trip, date))
                }

                tripActivities
            } catch (e: Exception) {
                throw e
            }
        }
    }

    suspend fun getUserTripCountForTheYear(): OperationResult<Int> {
        val functionTag = "GetUserTripCountForTheYear"

        return withContext(Dispatchers.IO) {
            try {
                Log.d(functionTag, "Initiating retrieval of user trip count for the year")

                val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
                Log.d(functionTag, "User is logged in with ID: $userId")

                val startOfYear = getStartOfYear()

                val activityCollectionRef = firestore.collection("users").document(userId).collection("tripActivity")
                    .whereGreaterThanOrEqualTo("date", startOfYear)  // Filters to get activities from the start of the year

                Log.d(functionTag, "Fetching trip count from Firestore")
                val querySnapshot = activityCollectionRef.get().await()

                val tripCount = querySnapshot.size()

                Log.d(functionTag, "Successfully retrieved trip count for the year: $tripCount")
                OperationResult.Success(tripCount)
            } catch (e: Exception) {
                Log.e(functionTag, "Exception occurred while retrieving trip count for the year: ${e.message}", e)
                OperationResult.Error(e)
            }
        }
    }



    suspend fun getTotalKilometersForYear(): Double {
        val allTrips = getUserTripActivities()

        return allTrips
            .filter { it.date.after(getStartOfYear()) }
            .sumOf { it.trip.distanceKm?: 0.0 }

    }

    private fun getStartOfYear(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }


}