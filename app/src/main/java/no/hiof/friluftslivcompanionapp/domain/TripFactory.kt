package no.hiof.friluftslivcompanionapp.domain

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.models.TripActivity
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import java.time.Duration
import java.util.Date

object TripFactory {

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

    //TODO Add validation and test.
    fun createTripActivity(trip: Trip, date: Date): TripActivity {
        val tripActivity = TripActivity(
            trip = trip,
            date = date
        )

        return tripActivity
    }

    //TODO Add validation and test.
    fun createTrip(
        tripType: TripType,
        tripRoute: List<LatLng>,
        tripDescription: String,
        tripDuration: Duration,
        tripDistance: Double,
        tripDifficulty: Int
    ): Trip? {
        val startNode = tripRoute.firstOrNull()
        val startGeoHash = startNode?.let {
            GeoFireUtils.getGeoHashForLocation(
                GeoLocation(
                    it.latitude,
                    it.longitude
                )
            )
        }
        val startLat = startNode?.latitude
        val startLng = startNode?.longitude

        return if (tripType == TripType.HIKE) {
            Hike(
                route = tripRoute,
                description = tripDescription,
                duration = tripDuration,
                distanceKm = tripDistance,
                difficulty = tripDifficulty,
                startGeoHash = startGeoHash,
                startLat = startLat,
                startLng = startLng
            )
        } else null
    }

    // This seems to be a duplication of 'createTrip'. Can we delete it?
    private fun createHike(
        tripRoute: List<LatLng>,
        tripDescription: String,
        tripDuration: Duration,
        tripDistance: Double,
        tripDifficulty: Int,
        startGeoHash: String?,
        startLat: Double?,
        startLng: Double?
    ): Hike {
        return Hike(
            route = tripRoute,
            description = tripDescription,
            duration = tripDuration,
            distanceKm = tripDistance,
            difficulty = tripDifficulty,
            startGeoHash = startGeoHash,
            startLat = startLat,
            startLng = startLng
        )
    }
}