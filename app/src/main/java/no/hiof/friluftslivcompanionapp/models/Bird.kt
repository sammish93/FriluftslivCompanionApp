package no.hiof.friluftslivcompanionapp.models

import java.time.LocalDateTime

class Bird(
    val speciesName: String?,
    val speciesNameScientific: String,
    val number: Int,
    var description: String?,
    val photoUrl: String?,
    val observationDate: LocalDateTime,
    val coordinates: Location
) : Animal(
    speciesName,
    speciesNameScientific,
    description,
    photoUrl
) {
    fun getBirdInfo(): BirdInfo {
        return BirdInfo(
            imageUrl = this.photoUrl,
            speciesName = this.speciesName,
            description = this.description
        )
    }

    override fun toString(): String {
        return StringBuilder().apply {
            append("\nname: $speciesName\n")
            append("scientific: $speciesNameScientific\n")
            append("How many: $number\n")
            append("photoUrl: $photoUrl\n")
            append("description: $description\n")
            append("observationDate: $observationDate\n")
            append("coordinates: $coordinates\n")
        }.toString()
    }
}