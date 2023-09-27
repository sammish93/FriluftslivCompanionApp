package no.hiof.friluftslivcompanionapp.models

data class Bird(
    val speciesNameEnglish: String?,
    val speciesNameNorwegian: String?,
    val speciesNameScientific: String,
    val descriptionEnglish: String?,
    val descriptionNorwegian: String?,
    val photoUrl: String?
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
            append(speciesNameEnglish)
            append(speciesNameScientific)
        }.toString()
    }
}