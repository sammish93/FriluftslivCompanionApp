package no.hiof.friluftslivcompanionapp.domain

import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import org.junit.runner.RunWith
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import org.junit.Assert
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class LocationFormatterTest {
    @Test
    fun testCalculateTotalDistanceMeter() {
        val nodeList = listOf(
            LatLng(0.0, 0.0),
            LatLng(1.0, 1.0),
            LatLng(2.0, 2.0),
            LatLng(3.0, 3.0)
        )

        val expectedTotalDistance = 0.0 + LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[0], nodeList[1]) +
                LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[1], nodeList[2]) +
                LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[2], nodeList[3])

        val result = LocationFormatter.calculateTotalDistanceMeters(nodeList)

        val tolerance = 1e-6
        assertEquals(expectedTotalDistance, result, tolerance)
    }

    @Test
    fun testCalculateTotalDistanceKilometers() {
        val nodeList = listOf(
            LatLng(0.0, 0.0), // Punkt 1
            LatLng(0.0, 1.0), // Punkt 2 (ca. 111.32 km fra Punkt 1)
            LatLng(0.0, 2.0), // Punkt 3 (ca. 111.32 km fra Punkt 2)
            LatLng(0.0, 3.0) // Punkt 4 (ca. 111.32 km fra Punkt 3)
        )

        val expectedDistanceKilometers = (0.0 + LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[0], nodeList[1]) +
                LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[1], nodeList[2]) +
                LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[2], nodeList[3]))/1000

        val result = LocationFormatter.calculateTotalDistanceKilometers(nodeList)

        assertEquals(expectedDistanceKilometers, result, 0.01) // Toleranse på 0.01 kilometer
    }

    @Test
    fun calculateTotalDistanceMeters_returnsZeroForSingleLocation(){
        val singleNodeList=listOf(LatLng(0.0,0.0))

        val result=LocationFormatter.calculateTotalDistanceMeters(singleNodeList)

        assertEquals(0.0,result,0.01)
    }


    @Test
    fun testCalculateDistanceMetersBetweenTwoNodes() {
        val nodeFrom = LatLng(0.0, 0.0)
        val nodeTo = LatLng(0.0, 1.0)

        val expectedDistanceMeters = 111320.0

        val result = LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeFrom, nodeTo)

        assertEquals(expectedDistanceMeters, result, 1.0)
    }



    @Test
    fun createRectangularBoundsFromLatLng_returnsExpectedResult(){
        val lat = 0.0
        val lng = 0.0
        val radius = 50.0

        val expectedSouthWest = LatLng(-4.496601677418301E-4, -4.496601677418301E-4)
        val expectedNorthEast = LatLng(4.496601677418301E-4, 4.496601677418301E-4)

        val result = LocationFormatter.createRectangularBoundsFromLatLng(lat, lng, radius)

        assertEquals(expectedSouthWest.latitude, result.southwest.latitude, 1.0e-10)
        assertEquals(expectedSouthWest.longitude, result.southwest.longitude, 1.0e-10)
        assertEquals(expectedNorthEast.latitude, result.northeast.latitude, 1.0e-10)
        assertEquals(expectedNorthEast.longitude, result.northeast.longitude, 1.0e-10)
    }

    @Test
    fun testGetRegionCodeByLocation() {
        val testLocations = listOf("Oslo", "Viken", "Innlandet", "Trøndelag")
        for (location in testLocations) {
            val result = LocationFormatter.getRegionCodeByLocation(location)
            val regionCode = result.first
            val message = result.second

            when (location) {
                "Oslo" -> {
                    Assert.assertEquals("NO-03", regionCode)
                    Assert.assertEquals("Success", message)
                }
                "Viken" -> {
                    Assert.assertEquals("NO-01,NO-02,NO-06", regionCode)
                    Assert.assertEquals("Success", message)
                }
                "Innlandet" -> {
                    Assert.assertEquals("NO-04,NO-05", regionCode)
                    Assert.assertEquals("Success", message)
                }
                "Trøndelag" -> {
                    Assert.assertEquals("NO-16,NO-17", regionCode)
                    Assert.assertEquals("Success", message)
                }

            }
        }
    }

}
//Jain(2018)