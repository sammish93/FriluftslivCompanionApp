package no.hiof.friluftslivcompanionapp.api
import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.domain.Geocoding
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GeocodingTest {

    @Test
    fun testGetRegionCode() = runBlocking {
        // latitud- og longitudverdier for testing
        val latitude = 59.9139
        val longitude = 10.7522

        // Kall på getRegionCode og få resultatet
        val result = Geocoding.getInstance().getRegionCode(latitude, longitude)

        // Sjekk at resultatet ikke er null og har riktig format (NO-XX)
        assertNotNull(result)
        assertTrue(result!!.startsWith("NO-"))
    }
}
