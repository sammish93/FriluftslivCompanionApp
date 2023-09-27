package no.hiof.friluftslivcompanionapp.data.repositories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.api.http_interface.EbirdApiService
import no.hiof.friluftslivcompanionapp.data.network.HttpClient
import no.hiof.friluftslivcompanionapp.models.SimpleBirdSighting
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import retrofit2.Response


class BirdRepository {

    // Base url for the eBird API.
    private val baseUrl = "https://api.ebird.org/"

    // Retrofit instance.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(HttpClient.instance) // Using the singleton instance from /networks/HttpClient.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // eBird API Service instance.
    private val eBirdApiService: EbirdApiService by lazy {
        retrofit.create(EbirdApiService::class.java)
    }

    // Function to get recent bird observations. This function can only be called from
    // another 'suspend' function or from a coroutine.
    suspend fun getRecentObservations(): List<SimpleBirdSighting>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = eBirdApiService.getRecentObservations().execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    println("Error: ${response.code()} - ${response.message()}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
