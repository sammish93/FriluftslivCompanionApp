package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.data.api.EBirdApi
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage

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
    suspend fun getRecentObservationsInOslo(
        regionCode: String="NO-03",
        language: SupportedLanguage=SupportedLanguage.ENGLISH,
        maxResult: Int=2
    ): Result<List<Bird>> {
        return EBirdApi().getRecentObservations(regionCode, language.code, maxResult)
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
}