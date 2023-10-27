package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.models.TripActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

        //Use this to add an activity to recent activity
        suspend fun addHikeActivityToUser(date: Date, tripId: String) {
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

                val tripDocumentRef = firestore.collection("trips").document(tripId)
                val tripDocument = tripDocumentRef.get().await()

                if (!tripDocument.exists()) {
                    throw IllegalStateException("Trip not found")
                }

                val tripData = tripDocument.data
                    ?: throw IllegalStateException("Data is missing from the trip")

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateString = dateFormat.format(date)

                val activityCollectionRef = firestore.collection("users").document(userId).collection("tripActivity")
                activityCollectionRef.document(dateString).set(tripData).await()
            } catch (e: Exception) {
                Log.e("Add Hike", "${e.message}")
                throw e

            }
        }



    suspend fun getAllUserActivities(): OperationResult<List<TripActivity>> {
        return withContext(Dispatchers.IO) {
            try {

                val userId = auth.currentUser?.uid
                    ?: return@withContext OperationResult.Error(Exception("No user logged in"))

                val activitySubcollectionRef = firestore.collection("users").document(userId).collection("tripActivity")
                val querySnapshot = activitySubcollectionRef.get().await()

                val tripActivities: MutableList<TripActivity> = mutableListOf()

                for (document in querySnapshot.documents) {
                    val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(document.id)
                    val tripValue = document.toObject(Trip::class.java)
                    if (dateKey != null && tripValue != null) {
                        val tripActivityMap: MutableMap<Date, Trip> = mutableMapOf()
                        tripActivityMap[dateKey] = tripValue
                        tripActivities.add(TripActivity(tripActivityMap))
                    }
                }

                OperationResult.Success(tripActivities)

            } catch (e: Exception) {
                OperationResult.Error(e)
            }
        }
    }


    suspend fun getUserActivityByDate(date: Date): Result<Trip?> = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid
                ?: return@withContext Result.failure(Exception("No user logged in"))

            val userDocumentRef = firestore.collection("users").document(userId)
            val userDocument = userDocumentRef.get().await()

            if (!userDocument.exists()) {
                return@withContext Result.failure(Exception("User not found"))
            }

            val tripActivityData = userDocument.toObject(TripActivity::class.java)?.tripActivity
                ?: return@withContext Result.failure(Exception("No trip activity found for user"))

            val tripForGivenDate = tripActivityData[date]

            if (tripForGivenDate != null) {
                return@withContext Result.success(tripForGivenDate)
            } else {
                return@withContext Result.failure(Exception("No trip activity found for the given date"))
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun getTotalDistanceCompleted(): Result<Double> = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUser?.uid
                ?: return@withContext Result.failure(Exception("No user logged in"))

            val userActivityCollectionRef = firestore.collection("users").document(userId).collection("tripActivity")
            val userActivities = userActivityCollectionRef.get().await()

            var totalDistance = 0.0

            for (document in userActivities) {
                val trip = document.toObject(Trip::class.java)
                totalDistance += trip.distanceKm ?: 0.0
            }

            return@withContext Result.success(totalDistance)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }




}