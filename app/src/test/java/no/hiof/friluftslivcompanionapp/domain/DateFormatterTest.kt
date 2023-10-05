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


class DateFormatterTest {
    @Test
    fun formatFromUnixTimestamp_returnsExpectedResult() = runBlocking {

        // Arrange
        // Timestamp represents 'Thu Oct 05 2023 09:05:23 GMT+0000'
        val unixTimestamp = "1696496723"

        // Act
        val localDate = DateFormatter.formatFromUnixTimestamp(unixTimestamp)

        // Assert
        assertEquals(LocalDate.of(2023, 10, 5), localDate)
    }

    @Test
    fun incorrectUnixTimestamp_returnsExpectedResult() = runBlocking {

        // Arrange
        // Timestamp represents 'Thu Oct 05 2023 09:05:23 GMT+0000'
        val unixTimestamp = "abcdefg123"

        // Act
        val exception = assertThrows(NumberFormatException::class.java) {
            val localDate = DateFormatter.formatFromUnixTimestamp(unixTimestamp)
        }


        // Assert
        assertEquals(NumberFormatException::class, exception::class)
    }
}