package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography

@Composable
fun CreateScreen(mainScreen: String, modifier: Modifier = Modifier) {
    Text(text = "This is the Add tab inside the $mainScreen screen!",
    style = CustomTypography.titleLarge,
    fontStyle = FontStyle.Italic,
    textAlign = TextAlign.Center,
    modifier = modifier.fillMaxSize()
    )
}