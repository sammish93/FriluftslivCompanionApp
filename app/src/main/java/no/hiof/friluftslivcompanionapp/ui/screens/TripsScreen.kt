package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.ui.components.items.cardItems
import no.hiof.friluftslivcompanionapp.ui.components.CardComponent
import no.hiof.friluftslivcompanionapp.ui.components.ListComponent
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography

@Composable
fun TripsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "This is the Trip screen!",
            style = CustomTypography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        ListComponent(items = cardItems)

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(cardItems) { cardItem ->
                CardComponent(cardItem)
            }
        }
    }
}