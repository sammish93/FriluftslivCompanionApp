package no.hiof.friluftslivcompanionapp.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.WikipediaApiService
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder
import no.hiof.friluftslivcompanionapp.models.api.SimpleWikipediaResponse
import no.hiof.friluftslivcompanionapp.models.api.WikipediaResponse

// Remember to have an option to retrieve an article in either English or Norwegian (because
// the app will support both languages).
class WikipediaApi {

    private val baseUrl = "https://en.wikipedia.org/"

    private val wikipediaApiService: WikipediaApiService by lazy {
        val retrofit = RetrofitBuilder.build(baseUrl)
        retrofit.create(WikipediaApiService::class.java)
    }

    suspend fun getBirdInfo(birdName: String): SimpleWikipediaResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = wikipediaApiService.getAdditionalBirdInfo(titles = birdName)
                if (response.isSuccessful) {
                    val wikipediaResponse = response.body()
                    wikipediaResponse?.let {
                        mapToSimpleResponse(it)
                    }
                } else null
            } catch (e: Exception) {
                null
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