package no.hiof.friluftslivcompanionapp.domain
import com.google.android.gms.maps.model.LatLng
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Duration
import java.util.Date


class TripFactoryTest {
    @Test
    fun testConvertTripDifficultyFromIntToStringEnglish() {
        assertEquals("Very Easy", TripFactory.convertTripDifficultyFromIntToString(1, SupportedLanguage.ENGLISH))
        assertEquals("Easy", TripFactory.convertTripDifficultyFromIntToString(2, SupportedLanguage.ENGLISH))
        assertEquals("Moderate", TripFactory.convertTripDifficultyFromIntToString(3, SupportedLanguage.ENGLISH))
        assertEquals("Difficult", TripFactory.convertTripDifficultyFromIntToString(4, SupportedLanguage.ENGLISH))
        assertEquals("Very Difficult", TripFactory.convertTripDifficultyFromIntToString(5, SupportedLanguage.ENGLISH))
        assertEquals("UNKNOWN", TripFactory.convertTripDifficultyFromIntToString(0, SupportedLanguage.ENGLISH))
    }

    @Test
    fun testConvertTripDifficultyFromIntToStringNorwegian() {
        assertEquals("Veldig Lett", TripFactory.convertTripDifficultyFromIntToString(1, SupportedLanguage.NORWEGIAN))
        assertEquals("Lett", TripFactory.convertTripDifficultyFromIntToString(2, SupportedLanguage.NORWEGIAN))
        assertEquals("Middels", TripFactory.convertTripDifficultyFromIntToString(3, SupportedLanguage.NORWEGIAN))
        assertEquals("Vanskelig", TripFactory.convertTripDifficultyFromIntToString(4, SupportedLanguage.NORWEGIAN))
        assertEquals("Veldig Vanskelig", TripFactory.convertTripDifficultyFromIntToString(5, SupportedLanguage.NORWEGIAN))
        assertEquals("UNKNOWN", TripFactory.convertTripDifficultyFromIntToString(0, SupportedLanguage.NORWEGIAN))
    }


    @Test
    fun testConvertTripDifficultyFromIntToStringUnknownLocale() {
        assertEquals("UNKNOWN", TripFactory.convertTripDifficultyFromIntToString(3, SupportedLanguage.GERMAN))
        assertEquals("UNKNOWN", TripFactory.convertTripDifficultyFromIntToString(1, SupportedLanguage.SPANISH))
    }
    @Test
    fun testCreateHikeTrip() {
        val route = listOf(LatLng(1.0, 2.0), LatLng(3.0, 4.0))
        val description = "A beautiful hike"
        val duration = Duration.ofHours(2).plusMinutes(30)
        val distance = 5.0
        val difficulty = 3

        val result = TripFactory.createTrip(TripType.HIKE, route, description, duration, distance, difficulty)

        assertTrue(result is Hike)
        if (result is Hike) {
            assertEquals(route, result.route)
            assertEquals(description, result.description)
            assertEquals(duration, result.duration)
            result.distanceKm?.let { assertEquals(distance, it, 0.01) }
            assertEquals(difficulty, result.difficulty)
        }
    }

    @Test
    fun testCreateTrip_WithEmptyRoute_ReturnsNull() {
        val tripRoute = emptyList<LatLng>()
        val tripDescription = "Description"
        val tripDuration = Duration.ofHours(2)
        val tripDistance = 10.0
        val tripDifficulty = 5
        val tripType = TripType.HIKE

        val result = TripFactory.createTrip(tripType, tripRoute, tripDescription, tripDuration, tripDistance, tripDifficulty)

        assertNull(result)
    }

    @Test
    fun testCreateTrip_WithNegativeDistance_ReturnsNull() {
        val tripRoute = listOf(LatLng(40.0, 20.0))
        val tripDescription = "Description"
        val tripDuration = Duration.ofHours(2)
        val tripDistance = -10.0
        val tripDifficulty = 5
        val tripType = TripType.HIKE

        val result = TripFactory.createTrip(tripType, tripRoute, tripDescription, tripDuration, tripDistance, tripDifficulty)

        assertNull(result)
    }

    @Test
    fun testCreateNonHikeTrip() {
        val route = listOf(LatLng(1.0, 2.0), LatLng(3.0, 4.0))
        val description = "A climbing adventure"
        val duration = Duration.ofHours(3).plusMinutes(15)
        val distance = 10.5
        val difficulty = 2

        val result = TripFactory.createTrip(TripType.CLIMB, route, description, duration, distance, difficulty)

        assertNull(result)
    }


    @Test
    fun testCreateTripActivity() {
        val trip = mock(Trip::class.java)

        `when`(trip.route).thenReturn(listOf(LatLng(0.0, 0.0), LatLng(1.0, 1.0)))
        `when`(trip.description).thenReturn("Test trip")
        `when`(trip.duration).thenReturn(Duration.ofHours(2))
        `when`(trip.distanceKm).thenReturn(10.0)
        `when`(trip.difficulty).thenReturn(5)
        val date = Date()

        val tripActivity = TripFactory.createTripActivity(trip, date)

        assertNotNull(tripActivity)
        if (tripActivity != null) {
            assertEquals(trip, tripActivity.trip)
        }
        if (tripActivity != null) {
            assertEquals(date, tripActivity.date)
        }
    }

}
//Jain(2018)