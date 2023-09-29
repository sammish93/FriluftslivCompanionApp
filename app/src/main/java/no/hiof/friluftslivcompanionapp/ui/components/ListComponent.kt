package no.hiof.friluftslivcompanionapp.ui.components

import ListItemComponent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import no.hiof.friluftslivcompanionapp.ui.components.items.CardItem


@Composable
fun <T> ListComponent(items: List<T>,
                      itemContent:@Composable (T, TextStyle)->Unit) {
        items.forEach{item->
            itemContent(item, MaterialTheme.typography.headlineLarge)
        }
}
