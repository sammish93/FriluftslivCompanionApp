package no.hiof.friluftslivcompanionapp.domain

import com.google.android.gms.maps.model.LatLng
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import java.time.Duration

object TripFactory {

    //TODO Test these functions.
    /**
     * Takes an integer from 1 to 5 and returns a string in the desired supported language.
     * @param difficulty An Int from 1 to 5. If another Int is supplied then "UNKNOWN" is returned.
     * @param locale Takes a SupportedLanguage enum value. The default value is
     * SupportedLanguage.ENGLISH.
     * @return Returns a string for each integer with 1 returning "Very Easy" in English, and 5
     * returning "Very Difficult".
     */
    fun convertTripDifficultyFromIntToString(
        difficulty: Int,
        locale: SupportedLanguage = SupportedLanguage.ENGLISH
    ): String {

        var stringToReturn = "UNKNOWN"
        when (locale) {
            SupportedLanguage.ENGLISH ->
                when (difficulty) {
                    5 -> stringToReturn = "Very Difficult"
                    4 -> stringToReturn = "Difficult"
                    3 -> stringToReturn = "Moderate"
                    2 -> stringToReturn = "Easy"
                    1 -> stringToReturn = "Very Easy"

                    else -> stringToReturn = "UNKNOWN"
                }

            SupportedLanguage.NORWEGIAN ->
                when (difficulty) {
                    5 -> stringToReturn = "Veldig Vanskelig"
                    4 -> stringToReturn = "Vanskelig"
                    3 -> stringToReturn = "Middels"
                    2 -> stringToReturn = "Lett"
                    1 -> stringToReturn = "Veldig Lett"

                    else -> stringToReturn = "UNKNOWN"
                }

            else -> stringToReturn = "UNKNOWN"
        }

        return stringToReturn
    }

    fun createTrip(tripType: TripType, tripRoute: List<LatLng>, tripDescription: String, tripDuration: Duration, tripDistance: Double, tripDifficulty: Int) : Trip? {
        if (tripType == TripType.HIKE) {
            return Hike(route = tripRoute, description = tripDescription, duration = tripDuration, distanceKm = tripDistance, difficulty = tripDifficulty)
        }
        else return null
    }

    private fun createHike(tripRoute: List<LatLng>, tripDescription: String, tripDuration: Duration, tripDistance: Double, tripDifficulty: Int) : Hike {
        return Hike(route = tripRoute, description = tripDescription, duration = tripDuration, distanceKm = tripDistance, difficulty = tripDifficulty)
    }
}