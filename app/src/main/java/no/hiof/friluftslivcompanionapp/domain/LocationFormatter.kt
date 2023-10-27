package no.hiof.friluftslivcompanionapp.domain

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.maps.android.SphericalUtil
import kotlin.math.sqrt

object LocationFormatter {


    /**
     * A function designed to return the total distance between 2 or more LatLng locations in
     * a list.
     * @param nodeList A List containing LatLng objects.
     * @return Returns the total distance between all locatons in meters. If only a single
     * LatLng object is present in the List then a value of 0.0 will be returned.
     */
    fun calculateTotalDistanceMeters(nodeList: List<LatLng>): Double {
        var totalDistance: Double = 0.0
        val listSize = nodeList.size

        if (listSize >= 2) {
            for (i in 0 until listSize - 1) {
                totalDistance += calculateDistanceMetersBetweenTwoNodes(
                    nodeList[i],
                    nodeList[i + 1]
                )
            }
        }

        return totalDistance
    }

    /**
     * A function designed to return the total distance between 2 or more LatLng locations in
     * a list.
     * @param nodeList A List containing LatLng objects.
     * @return Returns the total distance between all locatons in kilometers. If only a single
     * LatLng object is present in the List then a value of 0.0 will be returned.
     */
    fun calculateTotalDistanceKilometers(nodeList: List<LatLng>): Double {
        var totalDistance: Double = 0.0
        val listSize = nodeList.size

        if (listSize >= 2) {
            for (i in 0 until listSize - 1) {
                totalDistance += calculateDistanceMetersBetweenTwoNodes(
                    nodeList[i],
                    nodeList[i + 1]
                )
            }
        }

        return totalDistance / 1000
    }

    /**
     * A function designed to return the distance in meters between two LatLng coordinates.
     * @param nodeFrom The location that is being measured from.
     * @param nodeTo The location that is being measured to.
     * @return Returns a Double value of the distance between the two locations in meters.
     */
    fun calculateDistanceMetersBetweenTwoNodes(nodeFrom: LatLng, nodeTo: LatLng): Double {
        val distance = FloatArray(1)
        Location.distanceBetween(
            nodeFrom.latitude,
            nodeFrom.longitude,
            nodeTo.latitude,
            nodeTo.longitude,
            distance
        )
        return distance[0].toDouble()
    }

    // This is based on code found here - https://code.luasoftware.com/tutorials/android/create-rectangularbounds-based-on-latlng-with-radius
    /**
     * A function designed to return a rectangular bound of a location. The radius of said bound is
     * calculated using a double modifier.
     * @param lat A Double representing latitude.
     * @param lng A double representing longitude.
     * @param radius A radius multiplier that has a default value of 50.0.
     * @return Returns a RectangularBounds object based on the supplied lat and lng values. This
     * object can be used by the Places API to set a location bias to default results to the user's
     * GPS location.
     */
    fun createRectangularBoundsFromLatLng(
        lat: Double,
        lng: Double,
        radius: Double = 50.0
    ): RectangularBounds {
        val distance = radius * sqrt(2.0)

        val latLng = LatLng(lat, lng)

        val southWest = SphericalUtil.computeOffset(latLng, distance, 225.0)
        val northEast = SphericalUtil.computeOffset(latLng, distance, 45.0)

        return RectangularBounds.newInstance(southWest, northEast)
    }
}