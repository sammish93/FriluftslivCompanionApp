package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.ui.components.items.CardItem

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
