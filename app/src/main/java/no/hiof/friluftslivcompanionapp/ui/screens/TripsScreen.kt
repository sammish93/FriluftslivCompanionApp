package no.hiof.friluftslivcompanionapp.ui.screens
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.friluftslivcompanionapp.ui.components.maps.GoogleMap
import no.hiof.friluftslivcompanionapp.viewmodels.MapViewModel

@Composable
fun TripsScreen(
    navController: NavController, modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel(),
    mapViewModel: MapViewModel
) {
    GoogleMap(mapViewModel)
}

