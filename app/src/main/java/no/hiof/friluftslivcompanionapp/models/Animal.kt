package no.hiof.friluftslivcompanionapp.models

// Abstract class that inherits a base abstract class (FloraFauna) and it inherited by a class
// that can be instantiated (e.g. Bird)
abstract class Animal(
    speciesName: String?,
    speciesNameScientific: String,
    description: String?,
    photoUrl: String?
) : FloraFauna(
    speciesName,
    speciesNameScientific,
    description,
    photoUrl
) {

    companion object {
        fun fromMap(map: Map<String, Any?>): Animal {
            return Bird.fromMap(map)
        }
    }
}