package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.ui.components.items.TripItem

@Composable
fun Carousel(
    trips: List<Hike>,
    currentPage: MutableState<Int>
) {
    val listState = rememberLazyListState()

    LaunchedEffect(currentPage.value) {
        listState.animateScrollToItem(currentPage.value)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(trips) { trip ->
            TripItem(trip = trip)
        }
    }
    Spacer(modifier = Modifier.height(18.dp))
    Indicators(size = trips.size, currentPage = currentPage)
}

@Composable
fun Indicators(size: Int, currentPage: MutableState<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(size) { index ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(
                        if (currentPage.value == index) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { currentPage.value = index }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}