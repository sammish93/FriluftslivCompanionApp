package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.FriluftslivApp
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.enums.WeatherType
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import java.time.LocalDate

@Composable
fun WeatherCard(weather: Weather) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
    ) {
        Text(
            text = "Elevated",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }
}

var weather = Weather(Location(44.123, 52.2), LocalDate.now(), 6.2, 21.4, WeatherType.SCATTERED_CLOUDS)
@Composable
private fun ThemePreview(
    typography: Typography,
    isDarkTheme: Boolean,
    content: @Composable () -> Unit
) {
    FriluftslivCompanionAppTheme(typography = CustomTypography, useDarkTheme = isDarkTheme) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun LightThemePreview() {
    Surface(tonalElevation = 5.dp) {
        ThemePreview(typography = CustomTypography, isDarkTheme = false) {
            WeatherCard(weather)
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun DarkThemePreview() {
    Surface(tonalElevation = 5.dp) {
        ThemePreview(typography = CustomTypography, isDarkTheme = true) {
            WeatherCard(weather)
        }
    }
}