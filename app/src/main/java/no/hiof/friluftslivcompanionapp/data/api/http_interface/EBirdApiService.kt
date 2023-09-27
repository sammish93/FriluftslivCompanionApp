package no.hiof.friluftslivcompanionapp.data.api.http_interface
import no.hiof.friluftslivcompanionapp.models.SimpleBirdSighting
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EBirdApiService {

    // NO-03 is the region code of Oslo.
    @GET("/v2/data/obs/NO-03/recent")
    fun getRecentObservations(
        @Query("sppLocale") language: String = "en"
    ): Call<List<SimpleBirdSighting>>
}