package no.hiof.friluftslivcompanionapp.ui.components.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.Lifelist

@OptIn(ExperimentalCoilApi::class)
@Composable
fun LifelistItem(
    lifeList: Lifelist,

    ) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(220.dp)
            .aspectRatio(0.59f),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)


    ){
        Column {
            Box(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .padding(4.dp)
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
            }

            Text(
                text = lifeList.sightings.species.speciesName ?: "",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }

    }

}