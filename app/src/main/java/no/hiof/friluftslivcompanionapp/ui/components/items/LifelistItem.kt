package no.hiof.friluftslivcompanionapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.Lifelist
import no.hiof.friluftslivcompanionapp.ui.components.cards.formatDate
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalCoilApi::class)
@Composable
fun LifelistItem(lifeList: Lifelist, height: Dp, aspectRatio: Float) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(height)
            .aspectRatio(aspectRatio),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = lifeList.sightings.species.photoUrl,
                    builder = {
                        placeholder(R.drawable.ic_launcher_background)
                    }
                ),
                contentDescription = lifeList.sightings.species.speciesName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f))
            ) {
                Text(
                    text = lifeList.sightings.species.speciesName ?: "",
                    style = CustomTypography.headlineSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    val dateString = dateFormat.format(lifeList.sightings.date)

                    Text(
                        text = stringResource(R.string.date_seen, dateString),
                        style = CustomTypography.titleSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}