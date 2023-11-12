package no.hiof.friluftslivcompanionapp.data.repositories

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryBounds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.FloraFaunaSighting
import no.hiof.friluftslivcompanionapp.models.Lifelist
import java.util.Calendar
import javax.inject.Inject

class LifelistRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    // Responsible for retrieving a single user's lifelist. Used by the UserRepository.
    suspend fun addSightingToLifeList(newSighting: FloraFaunaSighting) {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No user currently signed in")

        val userId = currentUser.uid
        val userDocumentRef = firestore.collection("users").document(userId)
        val lifelistSubcollectionRef = userDocumentRef.collection("lifelist")


        val sightingData = newSighting.toMap()

        incrementYearlySpeciesCount(currentUser.uid)


        lifelistSubcollectionRef.add(sightingData).await()
    }


    suspend fun getAllItemsInLifeList(): List<Lifelist> {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No user currently signed in")

        val userId = currentUser.uid
        val userDocumentRef = firestore.collection("users").document(userId)
        val lifelistSubcollectionRef = userDocumentRef.collection("lifelist")

        val snapshot = lifelistSubcollectionRef.get().await()

        return snapshot.documents.mapNotNull { document ->
            FloraFaunaSighting.fromMap(document.data as Map<String, Any>)?.let { sighting ->
                Lifelist(sightings = sighting)
            }
        }
    }

    suspend fun getSightingsNearLocation(geoPoint: GeoPoint, radiusInKm: Double, limit: Int): OperationResult<List<FloraFaunaSighting>> {
        return try {
            val radiusInMeter = radiusInKm * 1000
            val center = GeoLocation(geoPoint.latitude, geoPoint.longitude)
            val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInMeter)
            val matchingSightings = getSightingsWithinGeoHashBounds(bounds, limit, radiusInMeter, center)

            OperationResult.Success(matchingSightings)
        } catch (e: Exception) {
            OperationResult.Error(e)
        }
    }
    private suspend fun getSightingsWithinGeoHashBounds(
        bounds: List<GeoQueryBounds>,
        limit: Int,
        radius: Double,
        center: GeoLocation
    ): List<FloraFaunaSighting> {
        val matchingSightings: MutableList<FloraFaunaSighting> = ArrayList()
        for (bound in bounds) {
            val sightingsQuery = firestore.collectionGroup("lifelist")
                .orderBy("location.geoHash")
                .startAt(bound.startHash)
                .endAt(bound.endHash)
                .limit(limit.toLong())

            val querySnapshot = sightingsQuery.get().await()
            val sightingsFromSnapshot = getSightingsFromQuerySnapshot(querySnapshot, radius, center)
            matchingSightings.addAll(sightingsFromSnapshot)
        }
        return matchingSightings
    }

    private fun getSightingsFromQuerySnapshot(
        querySnapshot: QuerySnapshot,
        radius: Double,
        center: GeoLocation
    ): List<FloraFaunaSighting> {
        val sightings: MutableList<FloraFaunaSighting> = ArrayList()
        for (doc in querySnapshot.documents) {
            val lat = doc.getDouble("location.lat") ?: continue
            val lng = doc.getDouble("location.lon") ?: continue

            val docLocation = GeoLocation(lat, lng)
            val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
            if (distanceInM <= radius) {
                val sightingMap = doc.data ?: continue
                FloraFaunaSighting.fromMap(sightingMap)?.let { sighting ->
                    sightings.add(sighting)
                }
            }
        }
        return sightings
    }



    suspend fun countSpeciesSightedThisYear(): Int {
        val allLifelists = getAllItemsInLifeList()


        val allSightings = allLifelists.map { it.sightings }

        val startOfYear = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val filteredSightingsForThisYear = allSightings.filter { sighting -> sighting.date.after(startOfYear) }

        return filteredSightingsForThisYear.map { it.species }.count()
    }

    private suspend fun incrementYearlySpeciesCount(uid: String): OperationResult<Unit> {
        return try {
            val userCollection = firestore.collection("users")
            val userDocument = userCollection.document(uid)


            userDocument.update("yearlySpeciesCount", FieldValue.increment(1)).await()

            OperationResult.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            OperationResult.Error(e)
        }
    }

}


