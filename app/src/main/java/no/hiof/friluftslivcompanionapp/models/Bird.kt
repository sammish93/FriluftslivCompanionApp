package no.hiof.friluftslivcompanionapp.models

class Bird(
    var speciesNameEnglish: String?,
    var speciesNameNorwegian: String?,
    var speciesNameScientific: String,
    var descriptionEnglish: String?,
    var descriptionNorwegian: String?,
    var photoUrl: String?
) : Animal(
    speciesNameEnglish,
    speciesNameNorwegian,
    speciesNameScientific,
    descriptionEnglish,
    descriptionNorwegian,
    photoUrl
) {

    override fun toString(): String {
        return StringBuilder().apply {
            append("name: $speciesNameEnglish ")
            append("scientific: $speciesNameScientific")
        }.toString()
    }
}