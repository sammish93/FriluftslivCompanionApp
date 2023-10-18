package no.hiof.friluftslivcompanionapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel
import no.hiof.friluftslivcompanionapp.viewmodels.WeatherViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WeatherSearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                userViewModel.searchPlaces(it)
            },
            label = { Text("Search for a place") },
            modifier = Modifier
                .fillMaxWidth()
        )

        LazyColumn {
            items(userViewModel.locationAutoFill) { result ->
                Text(
                    text = result.address,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            userViewModel.setSelectedLocationValues(
                                city = result.address,
                                coordinates = result.placeId,
                                regionCode = ""
                            )
                        }
                        .padding(16.dp)
                )

            }
        }
    }
}



