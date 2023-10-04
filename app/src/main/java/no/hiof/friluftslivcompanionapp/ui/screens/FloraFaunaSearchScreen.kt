package no.hiof.friluftslivcompanionapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.FloraFaunaViewModel

private const val TAG = "FFSearchScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloraFaunaSearchScreen(

    searchBy: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: FloraFaunaViewModel = viewModel()
) {
    var locationQuery by remember { mutableStateOf("") }
    // State to hold the location query
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // TextField for manual location input
        TextField(
        value = locationQuery,
        onValueChange = {
            locationQuery = it
        },
        label = { Text("Enter location") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=86.dp)
            .onKeyEvent {
                // Detect tab key press and initiate location search
                if (it.key == Key.Tab) {
                    // Call a function to start the location search
                    startLocationSearch(locationQuery)
                    return@onKeyEvent true
                }
                false
            }
    )
        Text(
            text = "This is the Search By $searchBy tab inside the Search screen!",
            style = CustomTypography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxSize()
        )
    }
}

fun startLocationSearch(query: String) {
    Log.d(TAG,"Location search started with query: $query")
}

