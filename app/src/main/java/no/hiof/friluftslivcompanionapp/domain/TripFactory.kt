package no.hiof.friluftslivcompanionapp.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

    fun createTripActivity(trip: Trip, date: Date): TripActivity? {
        if (trip == null || date == null) {
            Log.e("TripFactory","Error: Trip or date is null")
            return null
        }
        if (trip.route.isEmpty() || trip.description.isNullOrEmpty() || trip.duration == null ||
            trip.distanceKm == null || trip.difficulty == null
        ) {
            Log.e("TripFactory", "Error: Trip data is invalid")
            return null
        }

        val tripActivity = TripActivity(
            trip = trip,
            date = date
        )

        return tripActivity
    }


    fun createTrip(
        tripType: TripType,
        tripRoute: List<LatLng>,
        tripDescription: String,
        tripDuration: Duration,
        tripDistance: Double,
        tripDifficulty: Int)
    : Trip? {
        val startNode = tripRoute.firstOrNull()

        if (tripRoute.isEmpty() || tripDescription.isEmpty() || tripDistance < 0
            || tripDifficulty < 1 || tripDuration.toHours() <= 0) {
            return null
        }

        val startGeoHash = startNode?.let { GeoFireUtils.getGeoHashForLocation(GeoLocation(it.latitude, it.longitude)) }
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

    fun changeIconColor(context: Context, iconResId: Int, difficulty: Int): Bitmap {
        val originalIcon = BitmapFactory.decodeResource(context.resources, iconResId)
        val coloredIcon = Bitmap.createBitmap(originalIcon.width, originalIcon.height, originalIcon.config)

        val canvas = Canvas(coloredIcon)
        val color = Paint().apply {
            colorFilter = pickColorFilter(difficulty)
        }
        canvas.drawBitmap(originalIcon, 0f, 0f, color)
        return coloredIcon
    }

    private fun getColor(color: String): Int {
        val hvs = floatArrayOf(0f, 1f, 1f)
        hvs[0] = when (color) {
            "red" -> BitmapDescriptorFactory.HUE_RED
            "orange" -> BitmapDescriptorFactory.HUE_ORANGE

            else -> {BitmapDescriptorFactory.HUE_GREEN}
        }
        return Color.HSVToColor(hvs)
    }

    private fun pickColorFilter(difficulty: Int): PorterDuffColorFilter {
        val mode = PorterDuff.Mode.SRC_IN
        return when (difficulty) {
            5, 4 -> PorterDuffColorFilter(getColor("red"), mode)
            3 -> PorterDuffColorFilter(getColor("orange"), mode)

            else -> PorterDuffColorFilter(getColor("green"), mode)
        }
    }
}