package no.hiof.friluftslivcompanionapp.ui.screens

import ListItemComponent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.items.cardItems
import no.hiof.friluftslivcompanionapp.ui.components.CardComponent
import no.hiof.friluftslivcompanionapp.ui.components.CustomTabsBar
import no.hiof.friluftslivcompanionapp.ui.components.ListComponent
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(navController: NavController, modifier: Modifier = Modifier) {

    Scaffold(
        topBar = {
            CustomTabsBar(
                mapOf(
                    "Trips" to Screen.TRIPS,
                    "Recent Activity" to Screen.TRIPS_RECENT_ACTIVITY,
                    "Create Trip" to Screen.TRIPS_ADD
                ), navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ) {
            TopBar("Topbar") {
                // Handle tilbakeknapp klikk her
            }

            Text(
                text = "This is the Trip screen!",
                style = CustomTypography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            ListComponent(items = cardItems) { cardItem, style ->
                ListItemComponent(cardItem, style) { it.title }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(cardItems) { cardItem ->
                    CardComponent(cardItem)
                }
            }
        }
    }
}