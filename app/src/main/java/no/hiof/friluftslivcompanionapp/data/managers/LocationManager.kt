package no.hiof.friluftslivcompanionapp.data.managers

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(
    private val activity: Activity,
    lifecycle: Lifecycle,
    private val locationUpdateCallback: (Location) -> Unit
): DefaultLifecycleObserver {
    init {
        lifecycle.addObserver(this)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 1000L
    )
        .setIntervalMillis(1000)
        .setMinUpdateIntervalMillis(5000)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                locationUpdateCallback(location)
            }
        }
    }

    fun initializeFusedLocationProviderClient() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    @Suppress("MissingPermission")
    override fun onResume(owner: LifecycleOwner) {
        if (hasLocationPermission()) {
            fusedLocationProviderClient
                .requestLocationUpdates(
                    locationRequest, locationCallback, Looper.getMainLooper())
        }
    }

    @Suppress("MissingPermission")
    fun startLocationUpdate() {
        if (hasLocationPermission()) {
            fusedLocationProviderClient
                .requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    /*
    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
     */

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}