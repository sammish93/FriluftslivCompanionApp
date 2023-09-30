package no.hiof.friluftslivcompanionapp.models

class Bird(
    var speciesName: String?,
    var speciesNameScientific: String,
    var number: Int,
    var description: String?,
    var photoUrl: String?,
    var coordinates: Location
) : Animal(
    speciesName,
    speciesNameScientific,
    description,
    photoUrl
) {

    override fun toString(): String {
        return StringBuilder().apply {
            append("\nname: $speciesName\n")
            append("scientific: $speciesNameScientific\n")
            append("How many: $number\n")
            append("photoUrl: $photoUrl\n")
            append("description: $description\n")
            append("coordinates: $coordinates\n")
        }.toString()
    }
}