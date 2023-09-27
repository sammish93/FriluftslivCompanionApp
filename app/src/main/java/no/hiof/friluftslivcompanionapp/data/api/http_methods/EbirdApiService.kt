package no.hiof.friluftslivcompanionapp.data.api.http_methods

import no.hiof.friluftslivcompanionapp.models.SimpleBirdSighting
import retrofit2.Call
import retrofit2.http.GET

interface EbirdApiService {

    @GET("/v2/data/obs/KZ/recent")
    fun getRecentObservations(): Call<List<SimpleBirdSighting>>
}