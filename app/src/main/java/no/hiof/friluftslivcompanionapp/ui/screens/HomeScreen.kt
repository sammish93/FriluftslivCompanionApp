package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.ui.components.CardComponent
import no.hiof.friluftslivcompanionapp.ui.components.Sensors
import no.hiof.friluftslivcompanionapp.ui.components.items.CardItem
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun HomeScreen(userViewModel: UserViewModel = viewModel(), modifier: Modifier = Modifier) {


    val dummyTrip = listOf(
        CardItem(R.drawable.ic_launcher_foreground, "berries"),
        CardItem(R.drawable.ic_launcher_background, "this mountain"),
        CardItem(R.drawable.ic_launcher_foreground, "mountain 2")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item {
            Text(text = "happening now")
        }
        item {
            ElevatedLazyRow(items = listOf(dummyTrip[0]))
        }
        item {
            ElevatedLazyRow(items = dummyTrip.slice(0..2))
        }
    }
}

@Composable
fun ElevatedLazyRow(items: List<CardItem>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(items) { index, item ->
            CardComponent(cardItem = item)
        }
    }
}