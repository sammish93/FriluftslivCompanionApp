package no.hiof.friluftslivcompanionapp.utils
import android.location.Location
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation

// Used for default location.
val oslo = LatLng(DefaultLocation.OSLO.lat, DefaultLocation.OSLO.lon)

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

fun getCameraPosition(userLocation: LatLng?, cameraZoom: Float): CameraPosition {
    val defaultPosition = CameraPosition.fromLatLngZoom(oslo, 10f)
    return userLocation?.let { CameraPosition.fromLatLngZoom(it, cameraZoom) } ?: defaultPosition
}
