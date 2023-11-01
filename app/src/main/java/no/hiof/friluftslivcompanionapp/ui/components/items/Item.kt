package no.hiof.friluftslivcompanionapp.ui.components.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun Item(headline: String, support: String, icon: Painter, iconDescription: String) {

    val defaultModifier = Modifier.padding(start = 8.dp)

    ListItem(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            ),
        leadingContent = {
            Icon(
                painter = icon,
                contentDescription = iconDescription,
                modifier = Modifier.size(24.dp)
            )
        },
        headlineContent = {
            Text(
                text = headline,
                fontWeight = FontWeight.Bold,
                modifier = defaultModifier
            )
        },
        supportingContent = {
            Text(
                text = support,
                modifier = defaultModifier
            )
        },
        colors = ListItemColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            headlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            overlineColor = Color.Unspecified,
            supportingTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            trailingIconColor = Color.Unspecified,
            disabledTrailingIconColor = Color.Unspecified,
            disabledLeadingIconColor = Color.Unspecified,
            disabledHeadlineColor = Color.Unspecified
        )
    )
}