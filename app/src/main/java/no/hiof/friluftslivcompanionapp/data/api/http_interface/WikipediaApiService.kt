package no.hiof.friluftslivcompanionapp.data.api.http_interface

import no.hiof.friluftslivcompanionapp.models.api.WikipediaResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WikipediaApiService {

    @GET("/w/api.php")
    fun getAdditionalBirdInfo(
        @Query("format") format: String = "json",
        @Query("action") action: String = "query",
        @Query("prop") prop: String = "extracts|pageimages|pageterms",
        @Query("piprop") piprop: String = "thumbnail",
        @Query("pithumbsize") pithumbsize: Int = 600,
        @Query("exintro") exintro: String = "",
        @Query("explaintext") explaintext: String = "",
        @Query("redirects") redirects: Int = 1,
        @Query("titles") titles: String
    ): Response<WikipediaResponse>
}