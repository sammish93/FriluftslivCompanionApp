package no.hiof.friluftslivcompanionapp.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.SettingsBar
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            SettingsBar(
                onClick = { navController.navigate(Screen.PROFILE_SETTINGS.name) },
                userViewModel = userViewModel
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = "This is the Profile screen! This screen will have a cogwheel icon which, when " +
                        "clicked, will navigate the user to the PROFILE_SETTINGS page",
                style = CustomTypography.titleLarge,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
            )
        }
    }
}
