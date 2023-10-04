package no.hiof.friluftslivcompanionapp.domain

import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.models.enums.WeatherType


import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalDateTime


class WeatherIconMapperTest {
    @Test
    fun mapIconWithDayString_returnsExpectedResult() = runBlocking {

        // Arrange
        val string = "50d"

        // Act
        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        // Assert
        assertEquals(WeatherType.MIST, iconAsEnum)
    }

    @Test
    fun mapIconWithNightString_returnsExpectedResult() = runBlocking {

        // Arrange
        val string = "50n"

        // Act
        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        // Assert
        assertEquals(WeatherType.MIST, iconAsEnum)
    }

    @Test
    fun mapIconWithAllPossibleDayStrings_returnsExpectedResult() = runBlocking {

        // Arrange
        val strings = listOf<String>("01d", "02d", "03d", "04d", "09d", "10d", "11d", "13d", "50d")
        val weatherType = listOf<WeatherType>(
            WeatherType.CLEAR_SKY,
            WeatherType.FEW_CLOUDS,
            WeatherType.SCATTERED_CLOUDS,
            WeatherType.BROKEN_CLOUDS,
            WeatherType.SHOWER_RAIN,
            WeatherType.RAIN,
            WeatherType.THUNDERSTORM,
            WeatherType.SNOW,
            WeatherType.MIST
        )

        // Act

        // Assert
        for (i in strings.indices) {
            assertEquals(weatherType[i], WeatherIconMapper.mapIconToEnum(strings[i]))
        }

    }

    @Test
    fun mapIconWithAllPossibleNightStrings_returnsExpectedResult() = runBlocking {

        // Arrange
        val strings = listOf<String>("01n", "02n", "03n", "04n", "09n", "10n", "11n", "13n", "50n")
        val weatherType = listOf<WeatherType>(
            WeatherType.CLEAR_SKY,
            WeatherType.FEW_CLOUDS,
            WeatherType.SCATTERED_CLOUDS,
            WeatherType.BROKEN_CLOUDS,
            WeatherType.SHOWER_RAIN,
            WeatherType.RAIN,
            WeatherType.THUNDERSTORM,
            WeatherType.SNOW,
            WeatherType.MIST
        )

        // Act

        // Assert
        for (i in strings.indices) {
            assertEquals(weatherType[i], WeatherIconMapper.mapIconToEnum(strings[i]))
        }

    }

    @Test
    fun mapIconWithAllPossibleDayStrings_returnsExpectedResultAsDrawableResource() = runBlocking {

        // Arrange
        val strings = listOf<String>("01d", "02d", "03d", "04d", "09d", "10d", "11d", "13d", "50d")
        val weatherType = listOf<Int>(
            WeatherType.CLEAR_SKY.resourcePath,
            WeatherType.FEW_CLOUDS.resourcePath,
            WeatherType.SCATTERED_CLOUDS.resourcePath,
            WeatherType.BROKEN_CLOUDS.resourcePath,
            WeatherType.SHOWER_RAIN.resourcePath,
            WeatherType.RAIN.resourcePath,
            WeatherType.THUNDERSTORM.resourcePath,
            WeatherType.SNOW.resourcePath,
            WeatherType.MIST.resourcePath
        )

        // Act

        // Assert
        for (i in strings.indices) {
            assertEquals(weatherType[i], WeatherIconMapper.mapIconToDrawableResource(strings[i]))
        }

    }

    @Test
    fun mapIconWithEmptyString_returnsDefaultValueAsResult() = runBlocking {

        // Arrange
        val string = ""

        // Act
        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        // Assert
        assertEquals(WeatherType.CLEAR_SKY, iconAsEnum)
    }

    @Test
    fun mapIconWithIncorrectString_returnsDefaultValueAsResult() = runBlocking {

        // Arrange
        val string = "abcdefg123"

        // Act
        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        // Assert
        assertEquals(WeatherType.CLEAR_SKY, iconAsEnum)
    }
}