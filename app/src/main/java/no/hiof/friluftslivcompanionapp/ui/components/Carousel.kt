package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.ui.components.items.TripItem

@Composable
fun Carousel(trips: List<Trip>) {
    LazyRow {
        items(trips) { trip ->
            TripItem(trip)
        }
    }
}