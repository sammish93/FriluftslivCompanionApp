
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.ui.components.items.CardItem

@Composable
fun <T>ListItemComponent(item: T, textStyle: TextStyle, displayText:(T) ->String) {
    Text (text = displayText(item), style= textStyle,modifier = Modifier.padding(16.dp))
}



