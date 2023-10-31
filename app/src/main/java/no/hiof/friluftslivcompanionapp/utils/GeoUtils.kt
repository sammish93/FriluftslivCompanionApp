package no.hiof.friluftslivcompanionapp.utils

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import no.hiof.friluftslivcompanionapp.models.Hike
import java.time.Duration


fun convertDocumentToHike(document: DocumentSnapshot): Hike {
    val routeList = convertRouteDataToLatLngList(document.get("route"))
    val duration = convertMapToDuration(document)

    return Hike(
        documentId = document.id,
        route = routeList,
        description = document.getString("description"),
        duration = duration,
        distanceKm = document.getDouble("distanceKm"),
        difficulty = document.getLong("difficulty")?.toInt(),
        startGeoHash = document.getString("startGeoHash"),
        startLat = document.getDouble("startLat"),
        startLng = document.getDouble("startLng")
    )
}

fun convertRouteDataToLatLngList(routeData: Any?): List<LatLng> {
    return if (routeData is List<*>) {
        routeData.mapNotNull { item ->
            if (item is Map<*, *>) {
                val lat = item["latitude"] as? Double
                val lng = item["longitude"] as? Double
                if (lat != null && lng != null) {
                    LatLng(lat, lng)
                } else null
            } else null
        }
    } else emptyList()
}

/**
 * Converts a map from a Firestore document to a Duration object.
 *
 * @param document The Firestore document containing the duration map.
 * @return A Duration object or null if the conversion is not possible.
 */
fun convertMapToDuration(document: DocumentSnapshot): Duration? {
    val durationMap = document["duration"] as? Map<*, *>
    val seconds = durationMap?.get("seconds") as? Long ?: 0L
    val nanos = durationMap?.get("nano") as? Long ?: 0L
    return Duration.ofSeconds(seconds, nanos)
}