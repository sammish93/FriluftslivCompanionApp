package no.hiof.friluftslivcompanionapp.ui.screens

import android.location.Geocoder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import java.util.Locale

@Composable
fun TripsScreen(
    navController: NavController, modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel(),
    userViewModel: UserViewModel
) {
    val userState by userViewModel.state.collectAsState()
    val geocoder = Geocoder(LocalContext.current, Locale.getDefault())
    val location = geocoder.getFromLocation(
        userState.lastKnownLocation?.latitude ?: DefaultLocation.OSLO.lat,
        userState.lastKnownLocation?.longitude ?: DefaultLocation.OSLO.lon,
        1
    )

    // https://en.wikipedia.org/wiki/ISO_3166-2:NO#:~:text=ISO%203166%2D2%3ANO%20is,coded%20in%20ISO%203166%2D1.
    // Region code can be created using a function ->
    // fun getRegionCode(county: String) : String {
    // when (county) {
    //     "Møre og Romsdal" -> return "NO-15"
    //     "blah blah" -> ...
    //     else -> return "NO-03"
    //     }
    // }

    Text(text = location.toString())
}

