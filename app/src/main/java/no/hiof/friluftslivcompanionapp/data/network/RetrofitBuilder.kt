package no.hiof.friluftslivcompanionapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit builder that delivers a specific instance of retrofit.
 */
object RetrofitBuilder {

    fun buildEBirdApi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.ebird.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(HttpClient.instance)
            .build()
    }

    fun buildWikipediaApi(languageCode: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://${languageCode}.wikipedia.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}