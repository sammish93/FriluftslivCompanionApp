package no.hiof.friluftslivcompanionapp.models

import com.google.firebase.firestore.DocumentId

class Hike(
    @DocumentId val documentId: String ="",
    val startLocation: Location,
    val endLocation: Location?,
    val description: String?,
    val duration: Long?,
    val distanceKm: Double?,
    val difficulty: Float?
) : Trip(
    startLocation,
    endLocation,
    description,
    duration,
    distanceKm,
    difficulty
) {
    fun copy(
        documentId: String = this.documentId,
        startLocation: Location = this.startLocation,
        endLocation: Location? = this.endLocation,
        description: String? = this.description,
        duration: Long? = this.duration,
        distanceKm: Double? = this.distanceKm,
        difficulty: Float? = this.difficulty
    ): Hike {
        return Hike(
            documentId,
            startLocation,
            endLocation,
            description,
            duration,
            distanceKm,
            difficulty
        )
    }
}