package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.maps.android.compose.*
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.ui.components.ListComponent
import no.hiof.friluftslivcompanionapp.ui.components.items.StyleListItem



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaSearchScreen(
    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel()
) {

    var locationName by remember { mutableStateOf("") }
    val birdResults by viewModel.birdResults.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),

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
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                singleLine = true
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // New button for "Use my location"
            Button(
                onClick = { /* Handle click action for "Use my location" */ },
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
            StyleListItem(bird, textStyle) { it.speciesName ?: "Unknown Bird" }
        }
    }
}

//GoogleMapsView(locationName, viewModel)

