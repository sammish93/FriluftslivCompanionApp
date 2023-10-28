package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Trip
import java.time.Duration
import javax.inject.Inject

class TripsRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    suspend fun createTrip(hike: Trip?): OperationResult<Unit> {
        return try {

            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")


            val tripDocument = firestore.collection("trips").document()


            if (hike != null) {
                tripDocument.set(hike.toMap()).await()
            }  else {
                throw IllegalStateException("Trip is null")
            }

            OperationResult.Success(Unit)
        } catch (e: Exception) {

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

    // This method will update all the documents in the database with these new values if it is called:
    // startGeoHash - needed for getting trips based on location.
    // startLat, startLng - the lat/lng of the first node in the 'route' list. I just added these
    // so its easier to get the start node, but its not necessary.
    suspend fun updateAllTripsWithGeoHashes(): OperationResult<Unit> {
        return try {
            val tripCollectionRef = firestore.collection("trips")
            val querySnapshot = tripCollectionRef.get().await()
            val batch = firestore.batch()

            for (document in querySnapshot.documents) {
                val routeValue = document.get("route")
                if (routeValue !is List<*> || routeValue.isEmpty()) continue

                val startNode = routeValue[0]
                if (startNode !is Map<*, *>) continue

                val lat = startNode["latitude"] as? Double ?: continue
                val lng = startNode["longitude"] as? Double ?: continue
                val geoHash = GeoFireUtils.getGeoHashForLocation(GeoLocation(lat, lng))

                val updates = mapOf(
                    "startGeoHash" to geoHash,
                    "startLat" to lat,
                    "startLng" to lng
                )
                val docRef = tripCollectionRef.document(document.id)
                batch.update(docRef, updates)
            }
            batch.commit().await()
            OperationResult.Success(Unit)

        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    /**
     * Fetches trips near a given location from Firestore.
     *
     * This function queries Firestore for trips that are within a specified radius
     * from a given location. It makes use of Geohashes to approximate area queries.
     * The query is performed in several steps to ensure correctness and performance.
     * The results are then converted to Trip objects and returned.
     *
     * @param geoPoint The center point from where to search for nearby trips.
     * @param radiusInKm The radius in kilometers around the center point to search for trips.
     * @param limit The maximum number of trips to retrieve per query.
     * @return An OperationResult containing a list of Trip objects if successful, or an exception if failed.
     */
    suspend fun getTripsNearUsersLocation(geoPoint: GeoPoint, radiusInKm: Double, limit: Int): OperationResult<List<Hike>> {
        return try {
            val radiusInMeter = radiusInKm * 1000
            val center = GeoLocation(geoPoint.latitude, geoPoint.longitude)
            val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInMeter)
            val matchingDocs: MutableList<Hike> = ArrayList()

            for (bound in bounds) {
                val trips = firestore.collection("trips")
                    .orderBy("startGeoHash")
                    .startAt(bound.startHash)
                    .endAt(bound.endHash)
                    .limit(limit.toLong())
                val querySnapshot = trips.get().await()

                for (doc in querySnapshot.documents) {
                    val lat = doc.getDouble("startLat") ?: continue
                    val lng = doc.getDouble("startLng") ?: continue
                    val docLocation = GeoLocation(lat, lng)
                    val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)

                    if (distanceInM <= radiusInMeter) {
                        val trip = convertDocumentToHike(doc)
                        matchingDocs.add(trip)
                    }
                }
            }

            OperationResult.Success(matchingDocs)
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }

    /**
     * Converts a DocumentSnapshot to a Hike object.
     *
     * This function extracts various fields from the provided DocumentSnapshot,
     * and creates a Hike object from the extracted values. It uses the
     * convertRouteDataToLatLngList function to convert the route data from the
     * document to a List of LatLng objects.
     *
     * @param document The DocumentSnapshot to convert.
     * @return A Hike object containing the data from the document.
     */
    private fun convertDocumentToHike(document: DocumentSnapshot): Hike {
        val routeList = convertRouteDataToLatLngList(document.get("route"))
        val duration = convertMapToDuration(document)

        return Hike(
            documentId = document.id,
            route = routeList,
            description = document.getString("description"),
            duration = duration,
            distanceKm = document.getDouble("distanceKm"),
            difficulty = document.getLong("difficulty")?.toInt(),
            startGeoHash = document.getString("startGeoHash"),
            startLat = document.getDouble("startLat"),
            startLng = document.getDouble("startLng")
        )
    }

    /**
     * Converts route data to a List of LatLng objects.
     *
     * This function takes an object which is expected to be a List of Maps,
     * where each Map contains latitude and longitude values. It iterates through
     * the list and the maps, extracting the latitude and longitude values, and
     * creates LatLng objects from those values. The resulting List of LatLng
     * objects is then returned.
     *
     * @param routeData The object containing route data.
     * @return A List of LatLng objects representing the route.
     */
    private fun convertRouteDataToLatLngList(routeData: Any?): List<LatLng> {
        return if (routeData is List<*>) {
            routeData.mapNotNull { item ->
                if (item is Map<*, *>) {
                    val lat = item["latitude"] as? Double
                    val lng = item["longitude"] as? Double
                    if (lat != null && lng != null) {
                        LatLng(lat, lng)
                    } else null
                } else null
            }
        } else emptyList()
    }

    private fun convertMapToDuration(document: DocumentSnapshot): Duration? {
        val durationMap = document["duration"] as? Map<*, *>
        val seconds = durationMap?.get("seconds") as? Long ?: 0L
        val nanos = durationMap?.get("nano") as? Long ?: 0L
        return Duration.ofSeconds(seconds, nanos)
    }
}
