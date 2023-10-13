package no.hiof.friluftslivcompanionapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.ui.layout.ContentScale

@Composable
fun <T> ListItemWithButtonsAndImg(
    item: T,
    textStyle: TextStyle,
    displayText: (T) -> String,
    fetchImage: (T) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .wrapContentHeight()
    ) {

        val imageUrl = fetchImage(item)
        if (imageUrl.isNotEmpty()) {
            val painter = rememberImagePainter(data = imageUrl)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .wrapContentHeight(),
                contentScale = ContentScale.Crop
            )
        }

        Text(text = displayText(item), style = textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top=8.dp))

        // Additional details button
        Button(
            onClick = { /* TODO: Handle click action for "details button */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Additional details for ${displayText(item)}")
        }
    }
}
