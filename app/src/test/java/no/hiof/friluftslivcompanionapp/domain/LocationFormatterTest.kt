package no.hiof.friluftslivcompanionapp.domain

import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import org.junit.runner.RunWith
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
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

        // Forventet total avstand
        val expectedTotalDistance = 0.0 + LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[0], nodeList[1]) +
                LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[1], nodeList[2]) +
                LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[2], nodeList[3])

        val result = LocationFormatter.calculateTotalDistanceMeters(nodeList)

        // Sammenlign resultatet med forventet verdi med en toleranse på 1e-6.
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
        //Arrange
        val singleNodeList=listOf(LatLng(0.0,0.0))

        //Act
        val result=LocationFormatter.calculateTotalDistanceMeters(singleNodeList)

        //Assert
        assertEquals(0.0,result,0.01)
    }


    @Test
    fun testCalculateDistanceMetersBetweenTwoNodes() {
        val nodeFrom = LatLng(0.0, 0.0) //Punkt 1
        val nodeTo = LatLng(0.0, 1.0) //// Punkt 2 (ca. 111.32 km fra Punkt 1)

        // Den forventede avstanden mellom p1 og p2 i meter
        val expectedDistanceMeters = 111320.0

        val result = LocationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeFrom, nodeTo)

        assertEquals(expectedDistanceMeters, result, 1.0) // Toleranse på 1 meter
    }



    @Test
    fun createRectangularBoundsFromLatLng_returnsExpectedResult(){
        // Arrange
        val lat = 0.0
        val lng = 0.0
        val radius = 50.0

        // Forventede grenseverdier (bounding coordinates)
        val expectedSouthWest = LatLng(-4.496601677418301E-4, -4.496601677418301E-4)
        val expectedNorthEast = LatLng(4.496601677418301E-4, 4.496601677418301E-4)

        // Act
        val result = LocationFormatter.createRectangularBoundsFromLatLng(lat, lng, radius)

        // Assert
        assertEquals(expectedSouthWest.latitude, result.southwest.latitude, 1.0e-10)
        assertEquals(expectedSouthWest.longitude, result.southwest.longitude, 1.0e-10)
        assertEquals(expectedNorthEast.latitude, result.northeast.latitude, 1.0e-10)
        assertEquals(expectedNorthEast.longitude, result.northeast.longitude, 1.0e-10)
    }

}