package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
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


@OptIn(ExperimentalCoilApi::class)
@Composable
fun ElevatedCardComponent(cardItem: CardItem) {
    val paddingValue = 10.dp

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()



    ) {
        Column {
            Image(
                painter = rememberImagePainter(data = cardItem.imageResourceId, builder = {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                }),
                contentDescription = cardItem.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()

            )
            Text(
                text = cardItem.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(top = paddingValue)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun Carousel2(cards: List<CardItem>) {
    val scrollState = rememberLazyListState()

    // Calculate the center item based on the current scroll position
    val centerIndex = (scrollState.firstVisibleItemIndex + scrollState.firstVisibleItemScrollOffset / 100)

    LazyRow(
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        itemsIndexed(cards) { index, card ->
            val scale = when {
                index == centerIndex -> 1.4f
                index == centerIndex + 1 -> 1.1f
                else -> 1f
            }


            val animatedScale = animateFloatAsState(targetValue = scale).value

            Box(modifier = Modifier.padding(8.dp)) {
                ElevatedCardComponent2(cardItem = card, scaleFactor = animatedScale)
            }
        }
    }
}



@OptIn(ExperimentalCoilApi::class)
@Composable
fun ElevatedCardComponent2(cardItem: CardItem, scaleFactor: Float) {


    Card(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
            .padding(vertical = 16.dp)
            .width(120.dp)
            .height(220.dp)
    ) {
        Column {
            Image(
                painter = rememberImagePainter(
                    data = cardItem.imageResourceId,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.ic_launcher_background)
                    }
                ),
                contentDescription = cardItem.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(
                text = cardItem.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagingApi::class)
@Composable
fun ImageCardCarousel(cards: List<CardItem>) {
    val pagerState: PagerState = rememberPagerState(pageCount = { cards.size })
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 3
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        ElevatedCardComponent3(cardItem = cards[page], width = itemWidth)
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ElevatedCardComponent3(cardItem: CardItem, width: Dp) {
    val paddingValue = 10.dp

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(width)
            .padding(horizontal = 4.dp)  // Adjusted padding here
    ) {
        Column {
            Image(
                painter = rememberImagePainter(data = cardItem.imageResourceId, builder = {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                }),
                contentDescription = cardItem.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            )
            Text(
                text = cardItem.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(top = paddingValue)
                    .fillMaxWidth()
            )
        }
    }
}
