package no.hiof.friluftslivcompanionapp.models

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId
import java.time.Duration

data class Hike(
    @DocumentId val documentId: String ="",
    val route: List<LatLng>,
    val description: String? = null,
    val duration: Duration? = null,
    val distanceKm: Double? = null,
    val difficulty: Int? = null
) : Trip(
    route,
    description,
    duration,
    distanceKm,
    difficulty
) {

    fun toMap(): Map<String, Any?> {

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

}