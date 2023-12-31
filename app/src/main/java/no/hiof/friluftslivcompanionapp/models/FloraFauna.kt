package no.hiof.friluftslivcompanionapp.models

// Base abstract class. Inherited by another abstract class (Animal), and then a class that can
// be instantiated (Bird).
abstract class FloraFauna(
    speciesName: String?,
    speciesNameScientific: String,
    description: String?,
    photoUrl: String?
) {
    abstract fun toMap(): Map<String, Any?>

    abstract val photoUrl: String?
    abstract val speciesNameScientific: String
    abstract val speciesName: String?
    abstract val description: String?
}