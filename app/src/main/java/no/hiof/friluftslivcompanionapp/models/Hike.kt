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

}