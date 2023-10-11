package no.hiof.friluftslivcompanionapp.ui.components.items;

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

/**
 * Composable function to display a styled list item.
 *
 * This function takes an item of type [T], a [TextStyle] for the text, and a [displayText] function
 * that defines the text to be displayed for the item. It formats and styles the item for display in a list.
 *
 * @param item The item to display in the list.
 * @param textStyle The style to be applied to the displayed text.
 * @param displayText A function that defines the text to be displayed for the given item.
 */
@Composable
fun <T>StyleListItem(item: T, textStyle: TextStyle, displayText:(T) ->String) {
    Text (text = displayText(item), style= textStyle,modifier = Modifier.padding(16.dp))
}



