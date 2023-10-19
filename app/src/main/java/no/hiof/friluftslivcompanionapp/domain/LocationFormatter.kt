package no.hiof.friluftslivcompanionapp.domain

import android.location.Location
import com.google.android.gms.maps.model.LatLng

object LocationFormatter {

    //TODO Test these functions.
    /**
     * A function designed to return the total distance between 2 or more LatLng locations in
     * a list.
     * @param nodeList A List containing LatLng objects.
     * @return Returns the total distance between all locatons in meters. If only a single
     * LatLng object is present in the List then a value of 0.0 will be returned.
     */
    fun calculateTotalDistanceMeters(nodeList: List<LatLng>) : Double {
        var totalDistance: Double = 0.0
        val listSize = nodeList.size

        if (listSize >= 2) {
            for (i in 0 until listSize - 1) {
                totalDistance += calculateDistanceMetersBetweenTwoNodes(nodeList[i], nodeList[i + 1])
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
    fun calculateTotalDistanceKilometers(nodeList: List<LatLng>) : Double {
        var totalDistance: Double = 0.0
        val listSize = nodeList.size

        if (listSize >= 2) {
            for (i in 0 until listSize - 1) {
                totalDistance += calculateDistanceMetersBetweenTwoNodes(nodeList[i], nodeList[i + 1])
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
    fun calculateDistanceMetersBetweenTwoNodes(nodeFrom: LatLng, nodeTo: LatLng) : Double {
        val distance = FloatArray(1)
        Location.distanceBetween(nodeFrom.latitude, nodeFrom.longitude, nodeTo.latitude, nodeTo.longitude, distance)
        return distance[0].toDouble()
    }
}