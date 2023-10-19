package no.hiof.friluftslivcompanionapp.api
import no.hiof.friluftslivcompanionapp.domain.Geocoding
import org.junit.Assert
import org.junit.Test


class GeocodingTest {

    @Test
    fun testGetRegionCode() {
        // latitud- og longitudverdier for testing
        val latitude = 59.9139
        val longitude = 10.7522

        // Kall på getRegionCode og få resultatet
        val result = Geocoding.getRegionCode(latitude, longitude)

        // Sjekk at resultatet ikke er null og har riktig format (NO-XX)
        Assert.assertNotNull(result)
        Assert.assertTrue(result!!.startsWith("NO-"))
    }
}
