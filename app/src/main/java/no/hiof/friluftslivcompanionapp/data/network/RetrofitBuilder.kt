package no.hiof.friluftslivcompanionapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton instance that is used to reuses the retrofit builder.
 */
object RetrofitBuilder {

    fun build(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}