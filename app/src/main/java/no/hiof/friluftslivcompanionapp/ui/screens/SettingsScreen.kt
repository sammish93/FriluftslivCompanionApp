package no.hiof.friluftslivcompanionapp.ui.screens

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.ui.components.TopBar
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val userState by userViewModel.state.collectAsState()

    val openLanguageDialogue = remember { mutableStateOf(false) }

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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LangAlertDialogue(
    userViewModel: UserViewModel = viewModel(),
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
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
