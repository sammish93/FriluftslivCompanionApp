package no.hiof.friluftslivcompanionapp.models

// Abstract class that inherits a base abstract class (FloraFauna) and it inherited by a class
// that can be instantiated (e.g. Bird)
abstract class Animal(
    speciesNameEnglish: String?,
    speciesNameNorwegian: String?,
    speciesNameScientific: String,
    descriptionEnglish: String?,
    descriptionNorwegian: String?,
    photoUrl: String?
) : FloraFauna(
    speciesNameEnglish,
    speciesNameNorwegian,
    speciesNameScientific,
    descriptionEnglish,
    descriptionNorwegian,
    photoUrl
) {
}