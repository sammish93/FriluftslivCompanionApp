package no.hiof.friluftslivcompanionapp.models

import com.google.firebase.firestore.DocumentId

data class Hike(
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

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "startLocation" to this.startLocation,
            "endLocation" to this.endLocation,
            "description" to this.description,
            "duration" to this.duration,
            "distanceKm" to this.distanceKm,
            "difficulty" to this.difficulty
        )
    }

}