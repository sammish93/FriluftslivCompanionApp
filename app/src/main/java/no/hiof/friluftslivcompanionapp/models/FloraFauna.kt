package no.hiof.friluftslivcompanionapp.models

// Base abstract class. Inherited by another abstract class (Animal), and then a class that can
// be instantiated (Bird).
abstract class FloraFauna(
    speciesNameEnglish: String?,
    speciesNameNorwegian: String?,
    speciesNameScientific: String,
    descriptionEnglish: String?,
    descriptionNorwegian: String?,
    photoUrl: String?
) {

}