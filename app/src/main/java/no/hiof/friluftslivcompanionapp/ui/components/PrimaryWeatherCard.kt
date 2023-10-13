package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.enums.WeatherType
import no.hiof.friluftslivcompanionapp.models.enums.WeatherUnit
import no.hiof.friluftslivcompanionapp.ui.theme.CustomTypography
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import java.time.LocalDate


@Composable
fun PrimaryWeatherCard(
    weather: Weather,
    units: WeatherUnit = WeatherUnit.METRIC,
    current: Boolean = true,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = modifier
                .padding(end = 12.dp)
                .weight(1f)) {
                Text(
                    text = if (weather.date == LocalDate.now() && current) "Current Weather" else DateFormatter.formatToPrettyStringWithoutYear(
                        weather.date
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                Row(
                    modifier = modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = when (units) {
                                WeatherUnit.DEFAULT -> "Temperature: ${weather.temperature} K"
                                WeatherUnit.IMPERIAL -> "Temperature: ${weather.temperature} °F"
                                else -> "Temperature: ${weather.temperature} °C"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = when (units) {
                                WeatherUnit.DEFAULT -> "Wind Speed: ${weather.windSpeed} m/s"
                                WeatherUnit.IMPERIAL -> "Wind Speed: ${weather.windSpeed} mi/s"
                                else -> "Wind Speed: ${weather.windSpeed} m/s"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier.shadow(12.dp, shape = RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = painterResource(id = weather.weatherType.resourcePath),
                    modifier = modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .fillMaxHeight(),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = "An icon that summarises the weather."
                )
            }
        }
    }
}