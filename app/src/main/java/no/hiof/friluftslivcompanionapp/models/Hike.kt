package no.hiof.friluftslivcompanionapp.models

class Hike(
    startLocation: Location,
    endLocation: Location?,
    description: String?,
    duration: Long?,
    distanceKm: Double?,
    difficulty: Float?
) : Trip(
    startLocation,
    endLocation,
    description,
    duration,
    distanceKm,
    difficulty
) {
}