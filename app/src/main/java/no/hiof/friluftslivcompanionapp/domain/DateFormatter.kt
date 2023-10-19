package no.hiof.friluftslivcompanionapp.domain

import android.content.res.Resources
import androidx.compose.ui.text.capitalize
import no.hiof.friluftslivcompanionapp.R
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * A static object used to contain functions for formatting dates.
 */
object DateFormatter {

    /**
     * @param unixTimestamp Takes a a unix timestamp as a parameter (e.g. "1697187432").
     * @return Returns a LocalDate object.
     */
    fun formatFromUnixTimestamp(unixTimestamp: String): LocalDate {
        val unixTimestamp = unixTimestamp.toLong()
        val instant = Instant.ofEpochSecond(unixTimestamp)
        val zoneId = ZoneId.systemDefault()

        return instant.atZone(zoneId).toLocalDate()
    }

    /**
     * A static function designed to take a LocalDate object and return a pretty String.
     * @param localDate Takes a LocalDate object.
     * @param locale Takes a SupportedLanguage enum value. The default value is
     * SupportedLanguage.ENGLISH.
     * @return Returns a String in the format "2021-09-13" -> "Tuesday 13th September".
     * In Norwegian this would be -> "Tirsdag 13 September" without the suffix.
     */
    fun formatToPrettyStringWithoutYear(
        localDate: LocalDate,
        locale: SupportedLanguage = SupportedLanguage.ENGLISH
    ): String {
        var localeObj: Locale? = null

        when (locale) {
            SupportedLanguage.ENGLISH -> localeObj = Locale.ENGLISH
            SupportedLanguage.NORWEGIAN -> localeObj = Locale("no")
            else -> return ""
        }

        val dayOfWeekFormatter = DateTimeFormatter.ofPattern("EEEE", localeObj)
        val dayOfMonth = localDate.dayOfMonth
        val monthFormatter = DateTimeFormatter.ofPattern("MMMM", localeObj)

        val dayOfWeek = localDate.format(dayOfWeekFormatter).capitalize()
        val month = localDate.format(monthFormatter).capitalize()

        if (localeObj == Locale.ENGLISH) {
            val dayOfMonthWithSuffix = when {
                dayOfMonth in 11..13 -> "${dayOfMonth}th"
                dayOfMonth % 10 == 1 -> "${dayOfMonth}st"
                dayOfMonth % 10 == 2 -> "${dayOfMonth}nd"
                dayOfMonth % 10 == 3 -> "${dayOfMonth}rd"
                else -> "${dayOfMonth}th"
            }

            return "$dayOfWeek $dayOfMonthWithSuffix $month"
        } else if (localeObj == Locale("no")) {
            return "$dayOfWeek $dayOfMonth $month"
        }

        return ""
    }

    /**
     * Takes a duration and two localised strings and outputs a 'pretty' string.
     * @param duration A time measurement as type Duration
     * @param hoursLocalised A string which should be passed as a string resource. Due to the
     * constraints of non-composable functions, stringResources should be passed here.
     * @param minutesLocalised A string which should be passed as a string resource. Due to the
     * constraints of non-composable functions, stringResources should be passed here.
     * @return Returns a pretty string. A Duration of 01HH30MM00SS would return "1 Hours 30 Minutes"
     * if hoursLocalised and minutesLocalised are passed in English.
     */
    fun formatDurationToPrettyString(
        duration: Duration,
        hoursLocalised: String,
        minutesLocalised: String
    ): String {
        val hoursUnit = duration.seconds / 3600
        val minutesUnit = (duration.seconds % 3600) / 60

        return "$hoursUnit $hoursLocalised $minutesUnit $minutesLocalised"
    }
}