package no.hiof.friluftslivcompanionapp.models

class Bird(
    speciesNameEnglish: String?,
    speciesNameNorwegian: String?,
    speciesNameScientific: String,
    descriptionEnglish: String?,
    descriptionNorwegian: String?,
    photoUrl: String?
) : Animal(
    speciesNameEnglish,
    speciesNameNorwegian,
    speciesNameScientific,
    descriptionEnglish,
    descriptionNorwegian,
    photoUrl
) {
}