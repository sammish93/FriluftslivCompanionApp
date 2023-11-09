package no.hiof.friluftslivcompanionapp.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography

@OptIn(ExperimentalCoilApi::class)
@Composable

fun <T> LifelistViewCard(
    item: T,
    textStyle: TextStyle,
    title: String,
    header: String,
    subHeader: String,
    subHeader2: String,
    fetchImage: (T) -> String,
    onMoreInfoClick: () -> Unit
){
    val imageUrl = fetchImage(item)

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onMoreInfoClick() }
    ) {
        if (imageUrl.isNotEmpty()) {
            val painter = rememberImagePainter(data = imageUrl)

            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Column(
                    ) {
                        Text(
                            text = title,
                            style = CustomTypography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        Text(
                            text = header,
                            style = CustomTypography.headlineSmall
                        )

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        Text(
                            text = subHeader,
                            style = CustomTypography.titleSmall,
                            fontStyle = FontStyle.Italic
                        )

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))

                        Text(
                            text = subHeader2,
                            style = CustomTypography.titleSmall,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Box {
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        } else {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .weight(1f)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp, top = 8.dp, end = 12.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = title,
                            style = CustomTypography.bodySmall
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        Text(
                            text = header,
                            style = CustomTypography.headlineSmall
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        Text(
                            text = subHeader,
                            style = CustomTypography.titleSmall,
                            fontStyle = FontStyle.Italic
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        Text(
                            text = subHeader2,
                            style = CustomTypography.titleSmall,
                            fontStyle = FontStyle.Italic
                        )




                    }
                }
            }
        }
    }


}

