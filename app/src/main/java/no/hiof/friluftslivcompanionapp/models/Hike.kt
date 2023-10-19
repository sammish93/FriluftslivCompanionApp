package no.hiof.friluftslivcompanionapp.models

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId
import java.time.Duration

class Hike(
    @DocumentId val documentId: String ="",
    val route: List<LatLng>,
    val description: String?,
    val duration: Duration?,
    val distanceKm: Double?,
    val difficulty: Int?
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
        duration: Duration? = this.duration,
        distanceKm: Double? = this.distanceKm,
        difficulty: Int? = this.difficulty
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