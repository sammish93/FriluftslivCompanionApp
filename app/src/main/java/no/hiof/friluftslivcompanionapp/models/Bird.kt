package no.hiof.friluftslivcompanionapp.models

import java.time.LocalDateTime

data class Bird(
    override val speciesName: String? = null,
    override val speciesNameScientific: String,
    val number: Int,
    var description: String? = null,
    override val photoUrl: String? = null,
    val observationDate: LocalDateTime,
    val coordinates: Location
) : Animal(
    speciesName,
    speciesNameScientific,
    description,
    photoUrl
) {
    fun getBirdInfo(): SpeciesInfo {
        return SpeciesInfo(
            imageUrl = this.photoUrl,
            speciesName = this.speciesName,
            description = this.description
        )
    }

    fun toMap(): Map<String, Any?> {
        val birdMap = mutableMapOf<String, Any>()

        speciesName?.let { birdMap["speciesName"] = it }
        birdMap["speciesNameScientific"] = speciesNameScientific
        birdMap["number"] = number
        description?.let { birdMap["description"] = it }
        photoUrl?.let { birdMap["photoUrl"] = it }
        birdMap["observationDate"] = observationDate.toString()
        birdMap["coordinates"] = coordinates.toMap()

        return birdMap

    }
    /*
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

     */
}