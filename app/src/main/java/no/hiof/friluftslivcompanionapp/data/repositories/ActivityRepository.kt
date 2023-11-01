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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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

            Log.d(functionTag, "TripActivity added successfully to Firestore for user: $userId")
        } catch (e: Exception) {
            Log.e(functionTag, "Error adding trip activity: ${e.message}", e)
            throw e
        }
    }

    suspend fun getUserTripActivities(): OperationResult<List<TripActivity>> {
        val functionTag = "GetUserTripActivities"

        return withContext(Dispatchers.IO) {
            try {
                Log.d(functionTag, "Initiating retrieval of user trip activities")

                val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
                Log.d(functionTag, "User is logged in with ID: $userId")

                val activityCollectionRef = firestore.collection("users").document(userId).collection("tripActivity")
                Log.d(functionTag, "Reference to 'tripActivities' collection obtained")

                Log.d(functionTag, "Fetching trip activities from Firestore")
                val querySnapshot = activityCollectionRef.get().await()

                val tripActivities: MutableList<TripActivity> = mutableListOf()


                Log.d(functionTag, "Processing documents from Firestore")

                for (document in querySnapshot.documents) {
                    Log.d(functionTag, "Processing document ID: ${document.id}")


                    val dateTimestamp = document.data?.get("date") as? com.google.firebase.Timestamp
                    val date = dateTimestamp?.toDate()

                    val tripMap = document.data?.get("trip") as? Map<String, Any?>
                    if (tripMap == null || date == null) {
                        Log.w(functionTag, "Data missing or invalid data type in document: ${document.id}")
                        continue
                    }

                    Log.d(functionTag, "Creating Trip object from document data")
                    val trip = Hike.fromMap(tripMap)

                    Log.d(functionTag, "Adding new TripActivity")
                    tripActivities.add(TripActivity(trip, date))
                }

                Log.d(functionTag, "Successfully compiled list of TripActivities. Total count: ${tripActivities.size}")
                OperationResult.Success(tripActivities)
            } catch (e: Exception) {
                Log.e(functionTag, "Exception occurred while retrieving trip activities: ${e.message}", e)
                OperationResult.Error(e)
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
        if (allTrips is OperationResult.Success) {

            return allTrips.data
                .filter { it.date.after(getStartOfYear()) }
                .sumOf { it.trip.distanceKm?: 0.0 }
        }
        return 0.0
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




    /*

    suspend fun getAllUserActivities(): OperationResult<List<TripActivity>> {
        return withContext(Dispatchers.IO) {
            try {

                val userId = auth.currentUser?.uid
                    ?: return@withContext OperationResult.Error(Exception("No user logged in"))

                val activitySubcollectionRef = firestore.collection("users").document(userId).collection("activity")


                val querySnapshot = activitySubcollectionRef.get().await()


                val tripActivityMap: MutableMap<Date, Trip> = mutableMapOf()
                for (document in querySnapshot.documents) {
                    val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(document.id)
                    val tripValue = document.toObject(Trip::class.java)
                    if (dateKey != null && tripValue != null) {
                        val tripActivityMap: MutableMap<Date, Trip> = mutableMapOf()
                        tripActivityMap[dateKey] = tripValue
                        tripActivities.add(TripActivity(tripActivityMap))
                    }
                }

                Result.success(TripActivity(tripActivityMap))

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

     */

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