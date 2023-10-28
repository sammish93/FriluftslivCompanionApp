package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Trip
import javax.inject.Inject

class TripsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    suspend fun createTrip(hike: Trip?): OperationResult<Unit> {
        return try {

            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")


            val hikeData = when (hike) {
                is Hike -> hike.toMap()
                else -> throw IllegalArgumentException("Unsupported trip type or trip is null")
            }


            val tripDocument = firestore.collection("trips").document()


            tripDocument.set(hikeData).await()


            OperationResult.Success(Unit)
        } catch (e: Exception) {

            Log.e("CreateTrip", "Error creating trip: ${e.message}", e)
            OperationResult.Error(e)
        }
    }


    suspend fun getTripById(tripId: String): OperationResult<Hike> {
            return try {
                val tripDocumentRef = firestore.collection("trips").document(tripId)
                val snapshot = tripDocumentRef.get().await()

                if (snapshot.exists()) {
                    val trip = snapshot.toObject(Hike::class.java)
                    if (trip != null) {

                        OperationResult.Success(trip)
                    } else {

                        OperationResult.Error(Exception("Trip could not be parsed."))
                    }
                } else {

                    OperationResult.Error(Exception("No trip found with the provided ID."))
                }
            } catch (e: Exception) {

                Log.e("TripsRepository", "Error getting trip with ID: $tripId", e)
                OperationResult.Error(e)
            }
    }

    suspend fun getAllTrips(): OperationResult<List<Hike>> {
        return try {

            val tripsCollectionRef = firestore.collection("trips")


            val querySnapshot = tripsCollectionRef.get().await()


            val trips = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Hike::class.java)?.apply {
                    //TODO assign the document metadata to what you need
                    //
                }
            }


            OperationResult.Success(trips)

        } catch (e: Exception) {

            OperationResult.Error(e)
        }
    }

    //TODO get trips based on user location??



}
