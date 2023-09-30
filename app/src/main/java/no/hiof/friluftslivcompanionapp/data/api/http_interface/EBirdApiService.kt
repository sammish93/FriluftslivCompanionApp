package no.hiof.friluftslivcompanionapp.data.api.http_interface
import no.hiof.friluftslivcompanionapp.models.api.SimpleBirdSighting
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EBirdApiService {

    @GET("/v2/data/obs/{regionCode}/historic/{year}/{month}/{day}")
    suspend fun getRecentObservations(
        @Path("regionCode") regionCode: String,
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int,
        @Query("sppLocale") language: String = SupportedLanguage.ENGLISH.code,
        @Query("maxResults") maxResult: Int
    ): Response<List<SimpleBirdSighting>>
}