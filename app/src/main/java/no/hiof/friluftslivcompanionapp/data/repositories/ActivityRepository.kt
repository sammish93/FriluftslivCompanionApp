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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

        //Use this to add an activity to recent activity
        suspend fun addTripActivityToUser(tripId: String) {
            val functionTag = "AddTripActivity"
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
                Log.d(functionTag, "User is logged in with ID: $userId")

                // Reference to the trip that the user has selected.
                val tripDocumentRef = firestore.collection("trips").document(tripId)

                // Get the specific trip document from Firestore.
                val tripDocument = tripDocumentRef.get().await()

                if (!tripDocument.exists()) {
                    Log.w(functionTag, "Trip document does not exist")
                    throw IllegalStateException("Trip not found")
                }

                Log.d(functionTag, "Trip document retrieved successfully")

                // Extract data from the document, and if it's null, throw an exception.
                val tripData = tripDocument.data ?: throw IllegalStateException("Data is missing from the trip")

                // Convert the retrieved data to a Trip object (Hike).
                val trip = Hike.fromMap(tripData)

                // Get today's date.
                val today = Date()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateString = dateFormat.format(today)

                // Create a TripActivity object.
                val tripActivity = TripActivity(trip, today)

                // Log the operation
                Log.d(functionTag, "Creating a new TripActivity for date: $dateString")

                // Add to the user's 'tripActivity' collection in Firestore.
                val userTripActivityRef = firestore.collection("users").document(userId).collection("tripActivity")
                userTripActivityRef.document(dateString).set(tripActivity.toMap()).await()

                Log.d(functionTag, "TripActivity added successfully to Firestore for user: $userId")
            } catch (e: Exception) {
                Log.e(functionTag, "Error adding trip activity: ${e.message}", e)
                throw e // Rethrow or handle the exception as needed.
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
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                Log.d(functionTag, "Processing documents from Firestore")

                for (document in querySnapshot.documents) {
                    Log.d(functionTag, "Processing document ID: ${document.id}")

                    val date = try {
                        dateFormat.parse(document.id) // Parsing date from the document ID
                    } catch (e: ParseException) {
                        Log.w(functionTag, "Date parsing failed for document ID: ${document.id}", e)
                        null
                    }

                    val tripMap = document.data?.get("trip") as? Map<String, Any?>
                    if (tripMap == null) {
                        Log.w(functionTag, "No 'trip' data found or invalid data type in document: ${document.id}")
                        continue // Skip this iteration if we encounter an issue here
                    }

                    if (date != null && tripMap != null) {
                        Log.d(functionTag, "Creating Trip object from document data")
                        val trip = Hike.fromMap(tripMap) // Constructing Hike from map data

                        Log.d(functionTag, "Adding new TripActivity for date: ${document.id}")
                        tripActivities.add(TripActivity(trip, date)) // Using Date object here
                    } else {
                        Log.w(functionTag, "Data missing for creating TripActivity in document: ${document.id}")
                    }
                }

                Log.d(functionTag, "Successfully compiled list of TripActivities. Total count: ${tripActivities.size}")
                OperationResult.Success(tripActivities)
            } catch (e: Exception) {
                Log.e(functionTag, "Exception occurred while retrieving trip activities: ${e.message}", e)
                OperationResult.Error(e)
            }
        }
    }


    /*

    suspend fun getAllUserActivities(): OperationResult<List<TripActivity>> {
        return withContext(Dispatchers.IO) {
            val logTag = "UserActivitiesLog" // Define a log tag

            try {
                Log.d(logTag, "Starting retrieval of user activities")

                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Log.e(logTag, "No user logged in")
                    return@withContext OperationResult.Error(Exception("No user logged in"))
                }

                Log.d(logTag, "Logged-in user ID: $userId")

                val activitySubcollectionRef = firestore.collection("users").document(userId).collection("tripActivity")
                Log.d(logTag, "Retrieving activities from Firestore for user: $userId")

                val querySnapshot = activitySubcollectionRef.get().await()

                val tripActivities: MutableList<TripActivity> = mutableListOf()

                for (document in querySnapshot.documents) {
                    Log.d(logTag, "Processing document: ${document.id}")

                    val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(document.id)
                    val tripValue = document.toObject(Hike::class.java)

                    if (dateKey != null && tripValue != null) {
                        Log.d(logTag, "Creating TripActivity for date: $dateKey")
                        val tripActivityMap: MutableMap<Date, Trip> = mutableMapOf()
                        tripActivityMap[dateKey] = tripValue
                        tripActivities.add(TripActivity(tripActivityMap))
                    } else {
                        Log.w(logTag, "Missing date or trip data in document: ${document.id}")
                    }
                }

                Log.d(logTag, "Successfully compiled list of TripActivities. Total count: ${tripActivities.size}")

                OperationResult.Success(tripActivities)

            } catch (e: Exception) {
                Log.e(logTag, "Error retrieving trip activities", e)
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