package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CustomLoadingScreen() {
    CircularProgressIndicator(
    modifier = Modifier
    .fillMaxWidth()
    .wrapContentSize(Alignment.Center)
    )
}