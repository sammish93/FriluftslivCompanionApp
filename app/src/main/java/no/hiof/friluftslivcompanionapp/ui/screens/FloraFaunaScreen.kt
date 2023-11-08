package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.domain.FloraFaunaMapper
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.cards.FloraFaunaCard
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun FloraFaunaScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel(),

) {

    val recentActivity by viewModel.lifeList.collectAsState()

    LaunchedEffect(true){
        viewModel.getUserLifeList()
    }
    //TODO: Add error handling for offline behaviour, if needed, when the screen has its composables etc
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        recentActivity?.let{list ->
            items(list){ item ->
                val subclass = FloraFaunaMapper.mapClassToEnum(item.sightings.species)
                val subclassToString =
                    subclass?.let { stringResource(it.label) }
                        ?: stringResource(R.string.unknown)

                FloraFaunaCard(
                        item = item,
                        textStyle = CustomTypography.titleLarge,
                        title = subclassToString,
                        header = item.sightings.species.speciesName ?: "",
                        subHeader = item.sightings.species.speciesNameScientific,
                        fetchImage = {it.sightings.species.photoUrl ?: "Photo of ${it.sightings.species.speciesName}" },
                        onMoreInfoClick = {
                            viewModel.updateSelectedSpeciesInfo(item.sightings.species)
                            navController.navigate(Screen.FLORA_FAUNA_ADDITIONAL_INFO.route)
                        }
                    )
                }

            }

        }




}