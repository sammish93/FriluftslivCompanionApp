package no.hiof.friluftslivcompanionapp.models

import com.google.android.gms.maps.model.LatLng
import java.time.Duration

// The variable 'duration' will be in seconds, with functions in the domain layer to format it to
// minutes/hours. The variable 'difficulty' will be from 1 to 5.

// NOTE: We might want to include a List of pictures relating to a trip where users could submit
// their own photos.
abstract class Trip(
    open val route: List<LatLng>,
    open val description: String?,
    open val duration: Duration?,
    open val distanceKm: Double?,
    open val difficulty: Int?,
) {
    abstract fun toMap(): Map<String, Any?>
}