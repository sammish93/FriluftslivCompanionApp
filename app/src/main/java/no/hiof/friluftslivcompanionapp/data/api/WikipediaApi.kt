package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.WikipediaApiService
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.api.SimpleWikipediaResponse
import no.hiof.friluftslivcompanionapp.models.api.WikipediaResponse
import no.hiof.friluftslivcompanionapp.data.network.Result
import retrofit2.Response


class WikipediaApi(private var language: String) {

    private val wikipediaApiService: WikipediaApiService by lazy {
        val retrofit = RetrofitBuilder.buildWikipediaApi(language)
        retrofit.create(WikipediaApiService::class.java)
    }

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

    private fun handleResponse(response: Response<WikipediaResponse>): Result<SimpleWikipediaResponse?> {
        return if (response.isSuccessful) {
            val wikipediaResponse = response.body()
            wikipediaResponse?.let {Result.Success(mapToSimpleResponse(it)) } ?: Result.Failure("No body in response")
        } else {
            Result.Failure("Unsuccessful response: ${response.errorBody()?.string()}")
        }
    }

    private fun mapToSimpleResponse(wikipediaResponse: WikipediaResponse): SimpleWikipediaResponse? {
        val page = wikipediaResponse.query.pages.values.firstOrNull() ?: return null
        return SimpleWikipediaResponse(
            extract = page.extract,
            thumbnail = page.thumbnail?.source
        )
    }
}