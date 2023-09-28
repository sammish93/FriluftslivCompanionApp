package no.hiof.friluftslivcompanionapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton instance that is used to reuses the retrofit builder.
 */
object RetrofitBuilder {

    fun buildEBirdApi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.ebird.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(HttpClient.instance)
            .build()
    }

    fun buildWikipediaApi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://en.wikipedia.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}