package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


/**
 * Composable function to display a list of items.
 *
 * This function takes a list of items of type [T] and an [itemContent] composable function that defines
 * how each item should be displayed within the list. It organizes and displays the list of items
 * using a [LazyColumn].
 *
 * @param items The list of items to display.
 * @param itemContent The composable function that defines the appearance of each item in the list.
 *                    It takes each item of type [T] and a [TextStyle] for the text style.
 *                    Typically, this is implemented using [StyleListItem Component].
 *
 */
@Composable
fun <T> ListComponent(items: List<T>, itemContent: @Composable (T, TextStyle) -> Unit) {
    LazyColumn {
        items(items) { item ->
            itemContent(item, MaterialTheme.typography.headlineLarge)
        }
    }
}

// When the screen size is wide (not compact) then there will be two columns of cards
// in each row.
@Composable
fun <T> ListComponentWide(items: List<T>, itemContent: @Composable (T, TextStyle) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
        items(items) { item ->
            itemContent(item, MaterialTheme.typography.headlineLarge)
        }
    })
}
