package no.hiof.friluftslivcompanionapp.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.Duration


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

    @Test
    fun formatDurationToPrettyString_returnsExpectedResult() {
        // Arrange
        val duration = Duration.ofHours(1).plusMinutes(30)
        val hoursLocalised = "Hours"
        val minutesLocalised = "Minutes"

        // Act
        val result = DateFormatter.formatDurationToPrettyString(duration, hoursLocalised, minutesLocalised)

        // Assert
        val expected = "1 Hours 30 Minutes"
        assertEquals(expected, result)
    }
}