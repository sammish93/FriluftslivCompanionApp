package no.hiof.friluftslivcompanionapp.utils

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentSnapshot
import no.hiof.friluftslivcompanionapp.models.Hike
import java.time.Duration

/**
 * Converts a DocumentSnapshot to a Hike object.
 *
 * This function extracts various fields from the provided DocumentSnapshot,
 * and creates a Hike object from the extracted values. It uses the
 * convertRouteDataToLatLngList function to convert the route data from the
 * document to a List of LatLng objects.
 *
 * @param document The DocumentSnapshot to convert.
 * @return A Hike object containing the data from the document.
 */
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

/**
 * Converts route data to a List of LatLng objects.
 *
 * This function takes an object which is expected to be a List of Maps,
 * where each Map contains latitude and longitude values. It iterates through
 * the list and the maps, extracting the latitude and longitude values, and
 * creates LatLng objects from those values. The resulting List of LatLng
 * objects is then returned.
 *
 * @param routeData The object containing route data.
 * @return A List of LatLng objects representing the route.
 */
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