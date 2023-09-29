package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.WikipediaApiService
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.api.SimpleWikipediaResponse
import no.hiof.friluftslivcompanionapp.models.api.WikipediaResponse
import no.hiof.friluftslivcompanionapp.data.network.Result
import retrofit2.Response

/**
 * The `WikipediaApi` class is responsible for interacting with the Wikipedia API.
 * It uses Retrofit to make network requests and Gson to parse the JSON responses.
 *
 * @property language The language in which the Wikipedia information should be fetched.
 * @property wikipediaApiService The service instance used to make API calls.
 *
 * This class provides a method to get additional bird information and map it to
 * `SimpleWikipediaResponse` objects.
 */
class WikipediaApi(private var language: String) {

    // Wikipedia API Service instance.
    private val wikipediaApiService: WikipediaApiService by lazy {
        val retrofit = RetrofitBuilder.buildWikipediaApi(language)
        retrofit.create(WikipediaApiService::class.java)
    }

    /**
     * Fetches additional bird information from Wikipedia.
     * It handles the response and returns a Result object.
     *
     * @param birdName The name of the bird for which the information should be fetched.
     * @return A Result object containing a SimpleWikipediaResponse or an error message.
     */
    suspend fun getAdditionalBirdInfo(birdName: String): Result<SimpleWikipediaResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = wikipediaApiService.getAdditionalBirdInfo(titles = birdName)
                handleResponse(response)
            } catch (e: Exception) {
                Result.Failure(e.message ?: "Unknown failure")
            }
        }
    }

    // This method handles the response from the Wikipedia API.
    // It checks if the response is successful and returns a Result object accordingly.
    private fun handleResponse(response: Response<WikipediaResponse>): Result<SimpleWikipediaResponse?> {
        return if (response.isSuccessful) {
            val wikipediaResponse = response.body()
            wikipediaResponse?.let {Result.Success(mapToSimpleResponse(it)) } ?: Result.Failure("No body in response")
        } else {
            Result.Failure("Unsuccessful response: ${response.errorBody()?.string()}")
        }
    }

    // This method maps a WikipediaResponse object to a SimpleWikipediaResponse object.
    private fun mapToSimpleResponse(wikipediaResponse: WikipediaResponse): SimpleWikipediaResponse? {
        val page = wikipediaResponse.query.pages.values.firstOrNull() ?: return null
        return SimpleWikipediaResponse(
            extract = page.extract,
            thumbnail = page.thumbnail?.source
        )
    }
}