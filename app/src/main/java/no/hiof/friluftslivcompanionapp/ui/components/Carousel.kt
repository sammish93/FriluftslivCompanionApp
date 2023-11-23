package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Inspiration taken from: https://levelup.gitconnected.com/animated-carousel-with-jetpack-compose-7406a5a2b246


@Composable
fun <T> Carousel(
    items: List<T>,
    currentPage: MutableState<Int>,
    itemContent: @Composable (T) -> Unit
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
        items(items) { item ->
            itemContent(item)
        }
    }
    Spacer(modifier = Modifier.height(18.dp))
    // Optionally, include indicators if needed
}