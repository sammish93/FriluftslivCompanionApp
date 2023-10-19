package no.hiof.friluftslivcompanionapp.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(
        text = "This is the Profile screen! This screen will have a cogwheel icon which, when " +
                "clicked, will navigate the user to the PROFILE_SETTINGS page",
        style = CustomTypography.titleLarge,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center,
    )
    Button(onClick = {  }) {
        Text("Request GPS Permission")
    }
}
