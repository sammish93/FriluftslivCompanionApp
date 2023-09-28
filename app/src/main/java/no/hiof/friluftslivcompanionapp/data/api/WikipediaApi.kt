package no.hiof.friluftslivcompanionapp.data.api

import no.hiof.friluftslivcompanionapp.data.api.http_interface.WikipediaApiService
import no.hiof.friluftslivcompanionapp.data.network.RetrofitBuilder

// Remember to have an option to retrieve an article in either English or Norwegian (because
// the app will support both languages).
class WikipediaApi {

    private val baseUrl = "https://en.wikipedia.org/"

    private val wikipediaApiService: WikipediaApiService by lazy {
        val retrofit = RetrofitBuilder.build(baseUrl)
        retrofit.create(WikipediaApiService::class.java)
    }

}