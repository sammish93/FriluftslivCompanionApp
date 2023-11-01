package no.hiof.friluftslivcompanionapp.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapUtilsTest {

    private lateinit var oslo: LatLng
    private lateinit var nodes: List<LatLng>
    private lateinit var lastKnown: Location

    @Before
    fun setUp() {
        oslo = LatLng(DefaultLocation.OSLO.lat, DefaultLocation.OSLO.lon)
        nodes = listOf(
            LatLng(59.911491, 10.757933),
            LatLng(60.472024, 8.468946),
            oslo
        )
        lastKnown = Mockito.mock(Location::class.java)
    }

    @Test
    fun findClosestNode_returnClosestNodeToTarget() {

        val target = LatLng(59.913868, 10.752245)
        val closestNode = findClosestNode(target, nodes)

        Assert.assertNotNull(closestNode)
        Assert.assertEquals(oslo, closestNode)
    }

    @Test
    fun distance_returnsCorrectDistance() {
        val from = oslo
        val to = LatLng(59.913868, 10.752245)
        val distance = distance(from, to)

        Assert.assertTrue("Distance should be positive", distance > 0)
    }

    @Test
    fun getLastKnownLocation_returnsCorrectLatLngFromLocation() {
        val simulatedLat = 59.913868
        val simulatedLng = 10.752245

        `when`(lastKnown.latitude).thenReturn(simulatedLat)
        `when`(lastKnown.longitude).thenReturn(simulatedLng)

        val result = getLastKnownLocation(lastKnown)
        Assert.assertNotNull(result)
        Assert.assertTrue(result!!.latitude == simulatedLat)
        Assert.assertTrue(result.longitude == simulatedLng)
    }
}