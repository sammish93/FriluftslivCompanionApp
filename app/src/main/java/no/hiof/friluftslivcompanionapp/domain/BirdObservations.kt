package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.data.api.EBirdApi
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * The `BirdObservations` class serves as a domain layer in the application architecture,
 * designed as a Singleton to ensure a single instance in the application.
 * It acts as an intermediary between the data layer (`EBirdApi`) and the presentation layer.
 * It is responsible for invoking methods from the data layer to fetch data, which then
 * can be passed to the presentation layer.
 *
 * To get an instance of this class, use the `getInstance` method`.
 */
class BirdObservations private constructor() {

    private val eBirdApi = EBirdApi()
    companion object {

        @Volatile
        private var instance: BirdObservations? = null

        fun getInstance(): BirdObservations {
            return instance ?: synchronized(this) {
                instance ?: BirdObservations().also { instance = it }
            }
        }
    }

    /**
     * This method creates an instance of `EBirdApi` and calls the `getRecentObservations`
     * method on it, passing the language code, region code, year, month, day, and maxResult as parameters.
     *
     * @param regionCode The region code where birds have been observed. Default is "NO-03".
     * @param year The year of the observations. Default is the current year.
     * @param month The month of the observations. Default is the current month.
     * @param day The day of the observations. Default is the current day.
     * @param maxResult The maximum number of results to be returned. Default is 1.
     * @return A `Result` object containing a list of `Bird` objects representing the recent bird
     * observations in the specified region, or an error if the operation fails.
     */
    suspend fun getRecentObservations(
        languageCode: SupportedLanguage=SupportedLanguage.ENGLISH,
        regionCode: String="NO-03",
        year: Int=LocalDate.now().year,
        month: Int=LocalDate.now().monthValue,
        day: Int=LocalDate.now().dayOfMonth,
        maxResult: Int=1
    ): Result<List<Bird>> {

        eBirdApi.language = languageCode
        return eBirdApi.getRecentObservations(languageCode, regionCode, year, month, day, maxResult)
    }

    /**
     * This method fetches bird observations between two dates.
     *
     * @param startDate The start date of the observation period.
     * @param endDate The end date of the observation period.
     * @param regionCode The code of the region where birds have been observed. Default is "NO-03".
     * @param maxResult The maximum number of results to be returned for each day in the range. Default is 1.
     * @return A `Result` object containing a list of `Bird` objects.
     */
    suspend fun getObservationsBetweenDates(
        languageCode: SupportedLanguage=SupportedLanguage.ENGLISH,
        startDate: LocalDate,
        endDate: LocalDate,
        regionCode: String="NO-03",
        maxResult: Int = 1
    ): Result<List<Bird>> {

        val validationResult = validateDate(startDate, endDate)
        if (validationResult is Result.Failure)
            return validationResult

        val days = getDaysBetween(startDate, endDate)
        return Result.Success(iterateOverDays(languageCode, startDate, days, regionCode, maxResult))
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
    fun <T> processBirdList(birds: List<Bird>?, action: (Bird) -> T?): List<T> {
        return birds?.mapNotNull(action) ?: emptyList()
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
        languageCode: SupportedLanguage,
        startDate: LocalDate,
        daysBetween: Int,
        regionCode: String,
        maxResult: Int
    ): List<Bird> {

        val allObservations = mutableListOf<Bird>()
        for (day in 0..daysBetween) {
            val date = startDate.plusDays(day.toLong())
            val result = getObservationsForDate(languageCode, date, regionCode, maxResult)
            if (result is Result.Success) allObservations.addAll(result.value)
        }
        return allObservations.toList()
    }

    private suspend fun getObservationsForDate(
        languageCode: SupportedLanguage,
        date: LocalDate,
        regionCode: String,
        maxResult: Int
    ): Result<List<Bird>> {

        return getRecentObservations(
            languageCode = languageCode,
            regionCode = regionCode,
            year = date.year,
            month = date.monthValue,
            day = date.dayOfMonth,
            maxResult = maxResult
        )
    }
}
