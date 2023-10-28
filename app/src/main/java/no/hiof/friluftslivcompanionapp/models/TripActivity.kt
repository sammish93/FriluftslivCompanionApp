package no.hiof.friluftslivcompanionapp.models

import java.util.Date

// Datetime vs Date - Datetime seems to cause compile errors with kotlin. Date supports time alse.
data class TripActivity(
    val trip: Trip,
    val date: Date
) {
    fun toMap(): Map<String, Any> {

        val result = mutableMapOf<String, Any>()

        result["trip"] = this.trip.toMap()
        result["date"] = this.date

        return result
    }
}
