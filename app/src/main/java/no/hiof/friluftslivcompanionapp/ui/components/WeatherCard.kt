package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.WeatherIconMapper
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.models.enums.WeatherType
import no.hiof.friluftslivcompanionapp.models.enums.WeatherUnit
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import java.time.LocalDate

@Composable
fun WeatherCard(weather: Weather, units: WeatherUnit = WeatherUnit.METRIC) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 340.dp, height = 150.dp)
    ) {
        Row(modifier = Modifier) {
            Column(modifier = Modifier) {
                Text(
                    text = if (weather.date == LocalDate.now()) "Current Weather" else DateFormatter.formatToPrettyStringWithoutYear(
                        weather.date
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
                Row(modifier = Modifier) {
                    Text(
                        text = when (units) {
                            WeatherUnit.DEFAULT -> "Temp: ${weather.temperature} K"
                            WeatherUnit.IMPERIAL -> "Temp: ${weather.temperature} °F"
                            else -> "Temp: ${weather.temperature} °C"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = when (units) {
                            WeatherUnit.DEFAULT -> "Wind: ${weather.windSpeed} m/s"
                            WeatherUnit.IMPERIAL -> "Wind: ${weather.windSpeed} mi/s"
                            else -> "Wind: ${weather.windSpeed} m/s"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Image(
                painter = painterResource(id = weather.weatherType.resourcePath),
                contentDescription = null // You can provide a content description here
            )
        }
    }
}

var weather = Weather(
    Location(44.123, 52.2),
    LocalDate.parse("2023-10-14"),
    6.2,
    21.4,
    WeatherType.THUNDERSTORM
)

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