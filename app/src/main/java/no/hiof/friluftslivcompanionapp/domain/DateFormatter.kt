package no.hiof.friluftslivcompanionapp.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * A static object used to contain functions for formatting dates.
 */
object DateFormatter {

    // Takes a String in the unix timestamp format and returns a LocalDate object
    fun formatFromUnixTimestamp(unixTimestamp: String): LocalDate {
        val unixTimestamp = unixTimestamp.toLong()
        val instant = Instant.ofEpochSecond(unixTimestamp)
        val zoneId = ZoneId.systemDefault()

        return instant.atZone(zoneId).toLocalDate()
    }
}