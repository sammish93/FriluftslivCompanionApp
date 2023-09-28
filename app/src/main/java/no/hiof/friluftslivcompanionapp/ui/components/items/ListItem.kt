
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.ui.components.items.CardItem

@Composable
fun ListItemComponent(item: CardItem) {
    Text (text = item.title, modifier = Modifier.padding(16.dp))
}



