package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.TripsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsRecentActivityScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TripsViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CustomTabsBar(
                viewModel, navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "This is the Recent Activity tab inside the Trips screen!",
                style = CustomTypography.titleLarge,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}