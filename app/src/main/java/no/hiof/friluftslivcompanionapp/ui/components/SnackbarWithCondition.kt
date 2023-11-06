package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SnackbarWithCondition(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String,
    condition: Boolean
) {
    if (condition) {
        LaunchedEffect(key1 = condition) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
        }
    }
}