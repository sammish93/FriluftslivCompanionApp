package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.ui.components.items.CardItem

/**
 * Composable function to display a card with image, title, and an info icon.
 *
 * @param cardItem The [CardItem] containing data for the card.
 */
@Composable
fun CardComponent(cardItem: CardItem) {
    val paddingValue = 10.dp
    val iconSize = 24.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // TODO: Handle click event to navigate to a new screen with more information
            }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValue)
        ) {
            // Image
            Image(
                painter = painterResource(id = cardItem.imageResourceId),
                contentDescription = stringResource(R.string.completed_hikes),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            // Title
            Text(
                text = cardItem.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = paddingValue)
            )

            // Icon
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(MaterialTheme.shapes.small)
                    .clickable {
                        // TODO: Handle icon click event
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.more_info_icon),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}



@Composable
fun Carousel(cards: List<CardItem>) {
    var index by remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scrolling logic
    LaunchedEffect(key1 = true, block = {
        while (isActive) {
            delay(3000)
            if (index == cards.size - 1) index = 0
            else index++
            scrollState.animateScrollToItem(index)
        }
    })

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(16.dp)) {
            LazyRow(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                itemsIndexed(cards) { idx, card ->
                    if (idx == index) {
                        // This is the Hero card.
                        ElevatedCardComponent(cardItem = card, isHero = true)
                    } else {
                        ElevatedCardComponent(cardItem = card)
                    }
                }
            }
        }
    }
}

@Composable
fun ElevatedCardComponent(cardItem: CardItem, isHero: Boolean = false) {
    val paddingValue = 10.dp
    val iconSize = 24.dp


    val width = if (isHero) 180.dp else 150.dp
    val height = if (isHero) 280.dp else 250.dp

    Card(
        modifier = Modifier
            .height(height)
            .width(width),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column {
            Image(
                painter = rememberImagePainter(data = cardItem.imageResourceId, builder = {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                }),
                contentDescription = cardItem.title,
                contentScale = ContentScale.Crop
            )
            Text(
                text = cardItem.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = paddingValue)
            )
        }
    }
}

