package no.hiof.friluftslivcompanionapp.models

import android.util.Log
import java.time.LocalDateTime
import java.time.ZoneId

data class Bird(
    override val speciesName: String? = null,
    override val speciesNameScientific: String,
    val number: Int,
    override var description: String? = null,
    override val photoUrl: String? = null,
    //val observationDate: LocalDateTime,
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
            speciesNameScientific = this.speciesNameScientific,
            description = this.description
        )
    }

    override fun toMap(): Map<String, Any?> {
        val birdMap = mutableMapOf<String, Any>()

        speciesName?.let { birdMap["speciesName"] = it }
        birdMap["speciesNameScientific"] = speciesNameScientific
        birdMap["number"] = number
        description?.let { birdMap["description"] = it }
        photoUrl?.let { birdMap["photoUrl"] = it }
       // birdMap["observationDate"] = observationDate.toString()
        birdMap["coordinates"] = coordinates.toMap()

        return birdMap

    }



    companion object {
        fun fromMap(map: Map<String, Any?>): Bird {
            val speciesName = map["speciesName"] as? String
            val speciesNameScientific = map["speciesNameScientific"] as String
            val number = (map["number"] as Long).toInt()
            val description = map["description"] as? String
            val photoUrl = map["photoUrl"] as? String
            val rawObservationDate = map["observationDate"]

            val observationDate: LocalDateTime? = when (rawObservationDate) {
                is com.google.firebase.Timestamp -> {
                    // Convert Timestamp to LocalDateTime
                    try {
                        rawObservationDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                    } catch (e: Exception) {
                        // Handle any exceptions during conversion
                        Log.e("DATE_CONVERSION", "Error converting Timestamp to LocalDateTime", e)
                        null
                    }
                }
                is Map<*, *> -> {
                    // Handle case where date is stored as a map
                    Log.e("DATE_CONVERSION", "Expected Timestamp, but found Map for observationDate")
                    null
                }
                else -> {
                    // Handle other potential formats or log an error
                    Log.e("DATE_CONVERSION", "Unexpected type for observationDate: ${rawObservationDate?.javaClass?.name}")
                    null
                }
            }



            val coordinates = Location.fromMap(map["coordinates"] as Map<String, Any?>)

            return Bird(
                speciesName = speciesName,
                speciesNameScientific = speciesNameScientific,
                number = number,
                description = description,
                photoUrl = photoUrl,
                //observationDate = rawObservationDate,
                coordinates = coordinates
            )
        }
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