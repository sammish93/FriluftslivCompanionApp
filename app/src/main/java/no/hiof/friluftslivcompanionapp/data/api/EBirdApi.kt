package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.EBirdApiService
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.api.SimpleBirdSighting
import no.hiof.friluftslivcompanionapp.models.api.SimpleWikipediaResponse
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * The `EBirdApi` class is responsible for interacting with the eBird API and Wikipedia API.
 * It uses Retrofit to make network requests and Gson to parse the JSON responses.
 *
 * @property eBirdApiService The service instance used to make API calls to eBird API.
 *
 * This class provides a method to get recent bird observations, enrich them with additional
 * information from Wikipedia, and map them to `Bird` objects.
 * The method `getRecentObservations` is a suspending function and should be called from a coroutine
 * or another suspending function.
 *
 * The `getBirdInformation` method is used to fetch additional bird information from Wikipedia API
 * based on the scientific name of the bird from the eBird API response.
 *
 * The `mapToBird` method is used to map `SimpleBirdSighting` objects to `Bird` objects, enriching
 * them with additional information fetched from Wikipedia.
 */
class EBirdApi(private val language: SupportedLanguage) {
    private val wikipediaApi = WikipediaApi(language.code)

    // eBird API Service instance.
    private val eBirdApiService: EBirdApiService by lazy {
        val retrofit = RetrofitBuilder.buildEBirdApi()
        retrofit.create(EBirdApiService::class.java)
    }

    // Function to get recent bird observations. This function can only be called from
    // another 'suspend' function or from a coroutine.
    suspend fun getRecentObservations(
        regionCode: String,
        year: Int,
        month: Int,
        day: Int,
        maxResult: Int
    ): Result<List<Bird>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eBirdApiService.getRecentObservations(
                    regionCode, year, month, day, language.code, maxResult
                )
                if (response.isSuccessful) {
                    val birds = response.body()?.map { mapToBird(it) }
                    birds?.let { Result.Success(it) } ?: Result.Failure("No birds in response")
                } else {
                    Result.Failure("Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Result.Failure("Exception: ${e.message}")
            }
        }
    }

    private suspend fun getBirdInformation(sighting: SimpleBirdSighting): SimpleWikipediaResponse? {
        val result = wikipediaApi.getAdditionalBirdInfo(sighting.sciName)
        return if (result is Result.Success) result.value else null
    }

    private suspend fun mapToBird(sighting: SimpleBirdSighting): Bird {
        val additionalInfo = getBirdInformation(sighting)
        return Bird(
            speciesName = sighting.comName,
            speciesNameScientific = sighting.sciName,
            number = sighting.howMany,
            description = additionalInfo?.extract,
            photoUrl = additionalInfo?.thumbnail,
            observationDate = formatObservationDate(sighting.obsDt),
            coordinates = Location(sighting.lat, sighting.lng)
        )
    }

    private fun formatObservationDate(observationDate: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return LocalDateTime.parse(observationDate, formatter)
    }
}