package no.hiof.friluftslivcompanionapp.ui.components.maps
import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.MapViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import java.io.IOException
import no.hiof.friluftslivcompanionapp.utils.findClosestNode
import no.hiof.friluftslivcompanionapp.utils.getCameraPosition
import no.hiof.friluftslivcompanionapp.utils.getLastKnownLocation
import no.hiof.friluftslivcompanionapp.utils.oslo

/**
 * A Composable function that displays a Google Map with a marker indicating the user's location.
 *
 * @param viewModel The [MapViewModel] which contains the state for last known location of the user.
 *
 * The map will default to a view centered on Oslo if the user's location is not available.
 * If the user's location is available, the map will animate and center on the user's location.
 * A marker will be placed on the user's location with a title "Location" and a snippet "You".
 */
@Composable
fun GoogleMap(viewModel: MapViewModel, tripsModel: TripsViewModel) {

    // State to hold nodes added by user taps.
    val nodes by tripsModel.nodes.collectAsState()

    // State to hold last known user location.
    val state by viewModel.state.collectAsState()
    val lastKnownLocation = state.lastKnownLocation

    val mapProperties = MapProperties(isMyLocationEnabled = lastKnownLocation != null)
    val userLocation = getLastKnownLocation(lastKnownLocation)

    val cameraPosition = getCameraPosition(userLocation, 14f)
    val cameraPositionState = rememberCameraPositionState { position = cameraPosition }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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

            // Animation for the camera if user position is changed.
            LaunchedEffect(userLocation) {
                userLocation?.let {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 14f))
                }
            }
        }

        InfoButtonWithPopup(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 8.dp, start = 8.dp)
        )

    }
}

@Composable
fun InfoButtonWithPopup(modifier: Modifier = Modifier) {
    var showPopup by remember { mutableStateOf(false) }

    val customShape: Shape = RoundedCornerShape(8.dp)
    
    // Info button
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, shape = customShape)
            .width(55.dp)
    ) {
        IconButton(
            onClick = { showPopup = true },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                modifier = Modifier.size(26.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    // Popup dialog.
    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text(text = "How to use the map") },
            text = { CardPopup() },
            confirmButton = {
                Button(onClick = { showPopup = false }) {
                    Text("Got it!")
                }
            }
        )
    }
}

@Composable
fun CardPopup() {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tap the map to add points and draw routes.",
                fontSize = 16.sp
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Hold on point to remove it from the route.",
                fontSize = 16.sp
            )
        }
    }
}

// Lorena´s code.
@Composable
fun GoogleMapsView(locationName: String, viewModel: FloraFaunaViewModel) {
    val location = getLocationFromName(locationName, LocalContext.current)
    val defaultLocation = LatLng(59.9139, 10.7522)
    val initialLocation = location ?: defaultLocation
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialLocation, 10f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 230.dp),
        cameraPositionState = cameraPositionState

    ) {
        location?.let {
            Marker(
                state = MarkerState(position = it),
                title = "Location",
                snippet = "Marker at specified location"
            )
        }
    }
}


private fun getLocationFromName(locationName: String, context: Context): LatLng? {
    val geocoder = Geocoder(context)
    var addressList: List<Address>? = null

    try {
        // Get the address list based on the location name
        //Should use this instead:
        // public List<Address> getFromLocation (double latitude,
        //                double longitude,
        //                int maxResults)
        addressList = geocoder.getFromLocationName(locationName, 1)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    // Check if we have a valid address
    if (!addressList.isNullOrEmpty()) {
        val address: Address = addressList[0]

        return LatLng(address.latitude, address.longitude)
    }
    return null
}


