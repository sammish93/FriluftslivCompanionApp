package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography


@Composable
fun BirdObservationList(birdResults: List<String>) {
    if (birdResults.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Bird Observations:",
                style = CustomTypography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display each bird observation in the list
            birdResults.forEach { birdObservation ->
                Text(
                    text = birdObservation,
                    style = CustomTypography.headlineLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}
