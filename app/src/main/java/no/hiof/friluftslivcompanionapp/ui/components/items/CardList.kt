package no.hiof.friluftslivcompanionapp.ui.components.items


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalCoilApi::class)
@Composable

fun <T> CardList(
    item: T,
    textStyle: TextStyle,
    displayText: (T) -> String,
    fetchImage: (T) -> String,
    onMoreInfoClick: (T) -> Unit
) {
    val imageUrl = fetchImage(item)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .padding(8.dp)
            .clickable { onMoreInfoClick(item) }


    ) {
        Box{
            if (imageUrl.isNotEmpty()) {
                val painter = rememberImagePainter(data = imageUrl)
                Box{
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { onMoreInfoClick(item) },
                        modifier = Modifier
                            .size(38.dp)
                            .padding(8.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                            imageVector = Icons.Default.Info,
                            contentDescription = "Clickable button for additional info"
                        )
                    }

                }

            } else {
                Box{
                    Text(
                        text = displayText(item),
                        style = textStyle,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .align(Alignment.Center)
                    )
                    Button(
                        onClick = { onMoreInfoClick(item) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(text = "Additional details for ${displayText(item)}")
                    }
                }

            }

        }
        Text(
            text = displayText(item),
            style = textStyle,
            modifier = Modifier
                .padding(top = 4.dp)
                .align(Alignment.CenterHorizontally)
        )

    }
}