package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun FloraFaunaAdditionalInfo (
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel(),
    navController: NavController
){
    val floraFaunaState by viewModel.floraFaunaState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopBar(
            title = "Back to results",
            onBackClick = {
                navController.popBackStack()
            }
        )
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Extra Information", style = CustomTypography.titleLarge)
                floraFaunaState.selectedSpeciesInfo?.let {
                    Image(
                        painter = rememberImagePainter(data = it.imageUrl),
                        contentDescription = "Photo of ${it.speciesName}",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .wrapContentHeight(),
                        contentScale = ContentScale.Crop
                    )

                    Text(text = "${it.speciesName}", style = CustomTypography.headlineLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "${it.description}", style = CustomTypography.bodyLarge)
                }

            }

        }
    }
}