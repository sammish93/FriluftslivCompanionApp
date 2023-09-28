package no.hiof.friluftslivcompanionapp.data.api.http_interface

import no.hiof.friluftslivcompanionapp.models.api.MediaWikiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaWikiApiService {

    @GET("API_ENDPOINT")
    fun getAdditionalBirdInfo(@Query("param") queryParam: String): Response<MediaWikiResponse>
}