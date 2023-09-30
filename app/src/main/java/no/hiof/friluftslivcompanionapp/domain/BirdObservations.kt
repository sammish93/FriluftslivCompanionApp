package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.data.api.EBirdApi
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * The `BirdObservations` class serves as a domain layer in the application architecture.
 * It acts as an intermediary between the data layer (`EBirdApi`) and the presentation layer.
 * It is responsible for invoking methods from the data layer to fetch data, which then
 * can be passed to the presentation layer.
 */
class BirdObservations {

    /**
     * This method creates an instance of `EBirdApi` and calls the `getRecentObservations`
     * method on it, passing the language code as a parameter.
     *
     * @param regionCode The code of the region where birds have been observed.
     * @param language The language in which the bird observations should be fetched.
     * @param maxResult The returned results are limited by this value.
     * @return A list of `Bird` objects representing the recent bird observations in Oslo.
     */
    suspend fun getRecentObservations(
        regionCode: String="NO-03",
        year: Int=2023,
        month: Int=9,
        day: Int=30,
        language: SupportedLanguage=SupportedLanguage.ENGLISH,
        maxResult: Int=1
    ): Result<List<Bird>> {

        val api = EBirdApi(language)
        return api.getRecentObservations(regionCode, year, month, day, language.code, maxResult)
    }

    suspend fun getObservationsBetweenDates(
        startDate: LocalDate,
        endDate: LocalDate,
        regionCode: String="NO-03",
        language: SupportedLanguage=SupportedLanguage.ENGLISH,
        maxResult: Int = 1
    ): Result<List<Bird>> {

        val validationResult = validateDate(startDate, endDate)
        if (validationResult is Result.Failure)
            return validationResult

        val days = getDaysBetween(startDate, endDate)
        val allObservations: MutableList<Bird>
        allObservations = iterateOverDays(startDate, days, regionCode, language, maxResult)

        return Result.Success(allObservations)
    }

    /**
     * This method can be used to process a list of Bird objects according to the developer's needs.
     * It takes a list of Bird objects and a lambda function, applies the lambda to each Bird object
     * in the list, and returns a list of the results.
     *
     * @param birds The list of Bird objects to be processed.
     * @param action The lambda function to apply to each Bird object in the list.
     * @return A list of results obtained by applying the lambda function to each Bird object.
     */
    fun processBirdList(birds: List<Bird>?, action: (Bird) -> String): List<String>? {
        if (birds != null) return birds.map(action)
        return null
    }

    private fun validateDate(startDate: LocalDate, endDate: LocalDate): Result<Unit> {
        return if (getDaysBetween(startDate, endDate) < 0)
            Result.Failure("Start date must be before end date!")
        else
            Result.Success(Unit)
    }

    private fun getDaysBetween(startDate: LocalDate, endDate: LocalDate): Int {
        return ChronoUnit.DAYS.between(startDate, endDate).toInt()
    }

    private suspend fun iterateOverDays(
        startDate: LocalDate,
        daysBetween: Int,
        regionCode: String,
        language: SupportedLanguage,
        maxResult: Int
    ): MutableList<Bird> {
        val allObservations = mutableListOf<Bird>()

        for (day in 0..daysBetween) {
            val date = startDate.plusDays(day.toLong())
            val result = getObservationsForDate(date, regionCode, language, maxResult)
            if (result is Result.Success) allObservations.addAll(result.value)
        }
        return allObservations
    }

    private suspend fun getObservationsForDate(
        date: LocalDate,
        regionCode: String,
        language: SupportedLanguage,
        maxResult: Int
    ): Result<List<Bird>> {

        return getRecentObservations(
            regionCode = regionCode,
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth,
            language = language,
            maxResult = maxResult
        )
    }
}
