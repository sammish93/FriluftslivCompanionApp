package no.hiof.friluftslivcompanionapp.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val userState by userViewModel.state.collectAsState()

    val locPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val context = LocalContext.current

    val openLanguageDialogue = remember { mutableStateOf(false) }
    val openLocDialogue = remember { mutableStateOf(false) }
    val openProfileDialogue = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(title = "Back To Profile", onBackClick = { navController.popBackStack() })
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                // Language selector.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.clickable {
                        openLanguageDialogue.value = true
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Change Language"
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        Text(
                            text = stringResource(R.string.lang_language) + " - ",
                            textAlign = TextAlign.Start
                        )

                        Text(
                            text = stringResource(userState.language.nameLocalized),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Change Language"
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Profile picture selector.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.clickable {
                        openProfileDialogue.value = true
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Update Profile Picture"
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        Text(
                            text = "Profile Picture",
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Change Profile Picture"
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // GPS Location updater.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Update GPS Location"
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        Text(
                            text = "GPS Location",
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Switch(
                        checked = locPermissionState.status.isGranted,
                        onCheckedChange = {
                            if (locPermissionState.status.shouldShowRationale) {
                                locPermissionState.launchPermissionRequest()
                            } else {
                                openLocDialogue.value = true
                            }
                        }
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

                // Dark mode selector.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = "Toggle Dark Mode"
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                        Text(
                            text = "Dark Mode",
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Switch(
                        checked = userState.isDarkMode,
                        onCheckedChange = {
                            userViewModel.updateDarkMode(!userState.isDarkMode)
                        }
                    )
                }
            }
        }

        when (openLanguageDialogue.value) {
            true -> {
                LangAlertDialogue(
                    onDismissRequest = { openLanguageDialogue.value = false },
                    onConfirmation = {
                        openLanguageDialogue.value = false
                    },
                    userViewModel = userViewModel
                )
            }

            else -> {}
        }

        when (openLocDialogue.value) {
            true -> {
                LocAlertDialogue(
                    onConfirmation = {
                        openLocDialogue.value = false
                    },
                )
            }

            else -> {}
        }

        when (openLanguageDialogue.value) {
            true -> {
                ProfileAlertDialogue(
                    onDismissRequest = { openLanguageDialogue.value = false },
                    onConfirmation = {
                        openLanguageDialogue.value = false
                    },
                    userViewModel = userViewModel
                )
            }

            else -> {}
        }
    }
}

@Composable
fun LangAlertDialogue(
    userViewModel: UserViewModel = viewModel(),
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val (selectedLanguage, onLanguageSelected) = remember { mutableStateOf(userViewModel.getLanguage()) }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.lang_select_language))
        },
        text = {
            Column(
                Modifier
                    .selectableGroup()
            ) {
                userViewModel.supportedLanguages.forEach { option ->
                    val langLabel = stringResource(option.nameLocalized)

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .selectable(
                                selected = (option == selectedLanguage),
                                onClick = {
                                    onLanguageSelected(option)
                                },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedLanguage),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = langLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(onClick = {
                userViewModel.updateLanguage(selectedLanguage)
                onConfirmation()
                coroutineScope.launch {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(
                            userViewModel.getLanguage().code
                        )
                    )
                }
            }) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@Composable
fun LocAlertDialogue(
    onConfirmation: () -> Unit,
) {

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.oops))
        },
        text = {
            Text(text = stringResource(R.string.loc_permission_declined))
        },
        onDismissRequest = {
            onConfirmation()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.okay))
            }
        }
    )
}

@Composable
fun ProfileAlertDialogue(
    userViewModel: UserViewModel = viewModel(),
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val (selectedLanguage, onLanguageSelected) = remember { mutableStateOf(userViewModel.getLanguage()) }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.lang_select_language))
        },
        text = {
            Column(
                Modifier
                    .selectableGroup()
            ) {
                userViewModel.supportedLanguages.forEach { option ->
                    val langLabel = stringResource(option.nameLocalized)

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .selectable(
                                selected = (option == selectedLanguage),
                                onClick = {
                                    onLanguageSelected(option)
                                },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (option == selectedLanguage),
                            onClick = null // null recommended for accessibility with screenreaders
                        )
                        Text(
                            text = langLabel,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(onClick = {
                userViewModel.updateLanguage(selectedLanguage)
                onConfirmation()
                coroutineScope.launch {
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(
                            userViewModel.getLanguage().code
                        )
                    )
                }
            }) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}
