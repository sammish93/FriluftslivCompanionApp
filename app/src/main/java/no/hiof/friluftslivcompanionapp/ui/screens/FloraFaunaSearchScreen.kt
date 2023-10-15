package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.ListComponent
import no.hiof.friluftslivcompanionapp.ui.components.items.ListItemWithButtonsAndImg
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel


@Composable
fun FloraFaunaSearchScreen(
    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val userState by userViewModel.state.collectAsState()

    if(searchBy.equals("Location" ,ignoreCase = true)){
    var locationName by remember { mutableStateOf("") }
    val birdResults by viewModel.birdResults.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = locationName,
                onValueChange = { locationName = it },
                placeholder = { Text(text = "Enter your region code") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                singleLine = true
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = { /* TODO: Handle click action for "Use my location" */
                    viewModel.viewModelScope.launch {
                        viewModel.searchBirdsByLocation("NO")
                    }},
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(text = "Use my location")
            }

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.searchBirdsByLocation(locationName)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(40.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search",
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)

                )
            }

        }
        ListComponent(birdResults) { bird, textStyle ->
            ListItemWithButtonsAndImg(
                bird,
                textStyle,
                displayText = { it.speciesName ?: "Unknown Bird" },
                fetchImage = { it.photoUrl ?: "" }
            ){
                navController.navigate(Screen.FLORA_FAUNA_ADDITIONAL_INFO.route)
            }
            }
        }
    }
    else if (searchBy.equals("Species", ignoreCase = true)) {

        Text(text = "Content for Species Search")
    }
}


//GoogleMapsView(locationName, viewModel)

