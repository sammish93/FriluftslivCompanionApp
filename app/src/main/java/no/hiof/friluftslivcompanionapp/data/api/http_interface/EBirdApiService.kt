package no.hiof.friluftslivcompanionapp.data.api.http_interface
import no.hiof.friluftslivcompanionapp.models.api.SimpleBirdSighting
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EBirdApiService {

    // NO-03 is the region code of Oslo.
    @GET("/v2/data/obs/NO-03/recent")
    suspend fun getRecentObservations(
        @Query("sppLocale") language: String = "en"
    ): Response<List<SimpleBirdSighting>>
}