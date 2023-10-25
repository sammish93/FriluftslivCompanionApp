package no.hiof.friluftslivcompanionapp.domain

import com.google.android.gms.maps.model.LatLng
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull

@RunWith(MockitoJUnitRunner::class)
class LocationFormatterTest {
    /*@Mock
    private lateinit var locationFormatter: LocationFormatter

    @Test
    fun testCalculateTotalDistanceMeters() {

        val nodeList = listOf(
            LatLng(0.0, 0.0),
            LatLng(1.0, 1.0),
            LatLng(2.0, 2.0)
        )

        `when`(locationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[0], nodeList[1])).thenReturn(111.32)
        `when`(locationFormatter.calculateDistanceMetersBetweenTwoNodes(nodeList[1], nodeList[2])).thenReturn(111.32)

        val result = locationFormatter.calculateTotalDistanceMeters(nodeList)

        assertEquals(222.64, result, 0.01)
    }*/


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
    fun createRectangularBoundsFromLatLng_returnsExpectedResult(){
        //Arrange
        val lat=0.0
        val lng=0.0
        val radius=50.0

        //Act
        val result=LocationFormatter.createRectangularBoundsFromLatLng(lat,lng,radius)

        //Assert
        assertNotNull(result)

    }


}