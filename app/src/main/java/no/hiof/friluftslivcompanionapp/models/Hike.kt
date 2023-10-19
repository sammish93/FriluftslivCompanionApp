package no.hiof.friluftslivcompanionapp.models

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId

class Hike(
    @DocumentId val documentId: String ="",
    val route: List<LatLng>,
    val description: String?,
    val duration: Long?,
    val distanceKm: Double?,
    val difficulty: Float?
) : Trip(
    route,
    description,
    duration,
    distanceKm,
    difficulty
) {
    fun copy(
        documentId: String = this.documentId,
        route: List<LatLng>,
        description: String? = this.description,
        duration: Long? = this.duration,
        distanceKm: Double? = this.distanceKm,
        difficulty: Float? = this.difficulty
    ): Hike {
        return Hike(
            documentId,
            route,
            description,
            duration,
            distanceKm,
            difficulty
        )
    }
}