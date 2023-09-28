package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.WikipediaApiService
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.api.SimpleWikipediaResponse
import no.hiof.friluftslivcompanionapp.models.api.WikipediaResponse
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.models.enums.Language


// Remember to have an option to retrieve an article in either English or Norwegian (because
// the app will support both languages).
class WikipediaApi(private val language: Language) {

    private val wikipediaApiService: WikipediaApiService by lazy {
        val retrofit = RetrofitBuilder.buildWikipediaApi()
        retrofit.create(WikipediaApiService::class.java)
    }

    suspend fun getAdditionalBirdInfo(birdName: String): Result<SimpleWikipediaResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = wikipediaApiService.getAdditionalBirdInfo(titles = birdName)
                if (response.isSuccessful) {
                    val wikipediaResponse = response.body()
                    wikipediaResponse?.let {
                        return@withContext Result.Success(mapToSimpleResponse(it))
                    } ?: return@withContext Result.Failure("No body in response")
                } else return@withContext Result.Failure("Unsuccessful response: ${response.errorBody()?.string()}")
            } catch (e: Exception) {
                return@withContext Result.Failure(e.message ?: "Unknown failure")
            }
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