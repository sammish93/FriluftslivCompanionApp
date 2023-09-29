package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.EBirdApiService
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.api.SimpleBirdSighting
import no.hiof.friluftslivcompanionapp.models.api.SimpleWikipediaResponse
import no.hiof.friluftslivcompanionapp.models.enums.Language
import java.lang.Exception

/**
 * The `EBirdApi` class is responsible for interacting with the eBird API.
 * It uses Retrofit to make network requests and Gson to parse the JSON responses.
 *
 * @property baseUrl The base URL for the eBird API.
 * @property eBirdApiService The service instance used to make API calls.
 *
 * This class provides a method to get recent bird observations and map them to `Bird` objects.
 * The method `getRecentObservations` is a suspending function and should be called from a coroutine
 * or another suspending function.
 */
class EBirdApi {

    // eBird API Service instance.
    private val eBirdApiService: EBirdApiService by lazy {
        val retrofit = RetrofitBuilder.buildEBirdApi()
        retrofit.create(EBirdApiService::class.java)
    }

    // Function to get recent bird observations. This function can only be called from
    // another 'suspend' function or from a coroutine.
    suspend fun getRecentObservations(language: String): List<Bird>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = eBirdApiService.getRecentObservations(language, 4)
                if (response.isSuccessful) {
                    response.body()?.map { mapToBird(it) }
                } else {
                    println("Error: ${response.code()} - ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
                null
            }
        }
    }

    private suspend fun getBirdInformation(sighting: SimpleBirdSighting): Result<SimpleWikipediaResponse?> {
        val api = WikipediaApi(Language.EN)
        return api.getAdditionalBirdInfo(sighting.sciName)
    }

    // Function used to map SimpleBirdSighting to a Bird object.
    private suspend fun mapToBird(sighting: SimpleBirdSighting): Bird {
        val additional = getBirdInformation(sighting)

        return Bird(
            speciesNameEnglish = sighting.comName,
            speciesNameScientific = sighting.sciName,
            speciesNameNorwegian = null,
            descriptionEnglish = when (additional) {
                is Result.Success -> additional.value?.extract
                is Result.Failure -> "Failed to fetch bird description: ${additional.message}."
            },
            descriptionNorwegian = null,
            photoUrl = null
        )
    }
}