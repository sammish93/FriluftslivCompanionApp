package no.hiof.friluftslivcompanionapp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
class DarkModeSelectorTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testDarkModeSelector() {
        composeTestRule.setContent {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Build,
                        contentDescription = stringResource(R.string.settings_toggle_dark_mode)
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Text(
                        text = stringResource(R.string.settings_dark_mode),
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = true,  // Sett ønsket tilstand for Switch
                    onCheckedChange = {}
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Toggle Dark Mode").assertExists()

        composeTestRule.onNodeWithContentDescription("Toggle Dark Mode").performClick()


    }
}
