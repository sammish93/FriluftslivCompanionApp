package no.hiof.friluftslivcompanionapp.data.api.http_interface
import no.hiof.friluftslivcompanionapp.models.api.SimpleBirdSighting
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EBirdApiService {

    @GET("/v2/data/obs/{regionCode}/recent")
    suspend fun getRecentObservations(
        @Path("regionCode") regionCode: String = "NO-03", // NO-03 is the region code of Oslo.
        @Query("sppLocale") language: String = SupportedLanguage.ENGLISH.code,
        @Query("maxResults") maxResult: Int = 2
    ): Response<List<SimpleBirdSighting>>
}