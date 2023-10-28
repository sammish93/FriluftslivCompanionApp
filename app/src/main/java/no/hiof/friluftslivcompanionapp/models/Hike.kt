package no.hiof.friluftslivcompanionapp.models

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId
import java.time.Duration

data class Hike(
    @DocumentId val documentId: String ="",
    override val route: List<LatLng>,
    override val description: String? = null,
    override val duration: Duration? = null,
    override val distanceKm: Double? = null,
    override val difficulty: Int? = null
) : Trip(
    route,
    description,
    duration,
    distanceKm,
    difficulty
) {

   override fun toMap(): Map<String, Any?> {

        val resultMap = mutableMapOf<String, Any?>()


        resultMap["documentId"] = this.documentId
        resultMap["route"] = this.route.map { latLng ->

            mapOf("latitude" to latLng.latitude, "longitude" to latLng.longitude)
        }


        this.description?.let { resultMap["description"] = it }
        this.duration?.let { resultMap["duration"] = it }
        this.distanceKm?.let { resultMap["distanceKm"] = it }
        this.difficulty?.let { resultMap["difficulty"] = it }

        return resultMap
   }

    companion object {
        fun fromMap(map: Map<String, Any?>): Hike {
            val routeList = map["route"] as? List<Map<String, Double>> ?: emptyList()
            val route = routeList.mapNotNull {
                val latitude = it["latitude"]
                val longitude = it["longitude"]
                if (latitude != null && longitude != null) {
                    LatLng(latitude, longitude)
                } else {
                    null
                }
            }

            // Constructing the Hike object with other properties as well.
            return Hike(
                documentId = map["documentId"] as? String ?: "",
                route = route,
                description = map["description"] as? String,
                duration = map["duration"] as? Duration,
                distanceKm = (map["distanceKm"] as? Number)?.toDouble(),
                difficulty = (map["difficulty"] as? Number)?.toInt()
            )
        }
    }


}