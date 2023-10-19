package no.hiof.friluftslivcompanionapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.ui.theme.md_theme_light_background
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
            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                userViewModel.searchPlaces(it)
            },
            label = { Text(text = "Search for a place") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = TextFieldDefaults.colors(),
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon"
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear Text",
                    modifier = Modifier.clickable {
                        text = ""
                        userViewModel.clearAutocompleteResults()
                    }
                )
            }
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



