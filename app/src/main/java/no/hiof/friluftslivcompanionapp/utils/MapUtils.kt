package no.hiof.friluftslivcompanionapp.utils
import android.location.Location
import com.google.android.gms.maps.model.LatLng

// Helper functions for the google map composable.
fun findClosestNode(target: LatLng, nodes: List<LatLng>): LatLng? {
    return nodes.minByOrNull { distance(target, it) }
}

fun distance(from: LatLng, to: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, results)
    return results[0]
}

fun getLastKnownLocation(lastKnown: Location?): LatLng?{
    return lastKnown?.let {
        LatLng(it.latitude, lastKnown.longitude)
    }
}