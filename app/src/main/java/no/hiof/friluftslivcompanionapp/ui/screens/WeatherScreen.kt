package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography

@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    Text(text = "This is the Weather screen!",
        style =  CustomTypography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxSize())
}