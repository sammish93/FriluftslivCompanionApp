package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaSearchScreen(searchBy: String, navController: NavController, modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            CustomTabsBar(
                mapOf(
                    Screen.FLORA_FAUNA to "Lifelist",
                    Screen.FLORA_FAUNA_SEARCH_LOCATION to "Search (By Location)",
                    Screen.FLORA_FAUNA_SEARCH_SPECIES to "Search (By Species)"
                ),  navController
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
                text = "This is the Search By $searchBy tab inside the Search screen!",
                style = CustomTypography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}