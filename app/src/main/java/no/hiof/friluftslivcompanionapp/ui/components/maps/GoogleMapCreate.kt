package no.hiof.friluftslivcompanionapp.ui.components.maps

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.utils.findClosestNode
import no.hiof.friluftslivcompanionapp.utils.getCameraPosition
import no.hiof.friluftslivcompanionapp.utils.getLastKnownLocation
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

/**
 * A Composable function that displays a Google Map with a marker indicating the user's location.
 *
 * @param viewModel The [UserViewModel] which contains the state for last known location of the user.
 *
 * The map will default to a view centered on Oslo if the user's location is not available.
 * If the user's location is available, the map will animate and center on the user's location.
 * A marker will be placed on the user's location with a title "Location" and a snippet "You".
 */
@Composable
fun GoogleMapCreate(
    viewModel: UserViewModel,
    tripsModel: TripsViewModel,
    modifier: Modifier = Modifier
) {

    // State to hold nodes added by user taps.
    val nodes by tripsModel.nodes.collectAsState()

    // State to hold information relevant to all Trips navigational pages.
    val tripsState by tripsModel.tripsState.collectAsState()

    // State to hold last known user location.
    val userState by viewModel.state.collectAsState()
    val lastKnownLocation = userState.lastKnownLocation

    val mapProperties =
        MapProperties(
            isMyLocationEnabled = userState.isLocationPermissionGranted,
            mapType = MapType.TERRAIN
        )
    val userLocation = getLastKnownLocation(lastKnownLocation)

    // Camera position defaults to Oslo if GPS coordinates cannot be retrieved.
    val cameraPosition = getCameraPosition(
        userLocation ?: LatLng(
            DefaultLocation.OSLO.lat,
            DefaultLocation.OSLO.lon
        ), if (userState.isLocationPermissionGranted) 14f else 5f
    )

    val cameraPositionState = rememberCameraPositionState { position = cameraPosition }


    GoogleMap(
        modifier = Modifier
            .fillMaxWidth(),
        properties = mapProperties,
        cameraPositionState = cameraPositionState,
        onMapLongClick = { latLng ->
            val closestNode = findClosestNode(latLng, nodes)
            if (closestNode != null) {
                tripsModel.removeNode(closestNode)
            }
        },
        onMapClick = { latLng ->
            tripsModel.addNode(latLng)
        }
    ) {
        // Add marker representing the nodes.
        nodes.forEach { node ->
            Marker(
                MarkerState(position = node),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                title = "Node",
                snippet = "$node"
            )
        }

        // Draw polyline if there are at least 2 nodes.
        if (nodes.size >= 2) {
            Polyline(
                points = nodes,
                color = Color.Red,
                width = 5f
            )
        }

        // Add marker on the users location.
        userLocation?.let { MarkerState(position = it) }?.let {
            Marker(state = it, title = "Location", snippet = "You")
        }
    }
}

