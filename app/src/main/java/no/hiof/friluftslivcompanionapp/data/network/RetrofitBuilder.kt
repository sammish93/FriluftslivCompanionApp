package no.hiof.friluftslivcompanionapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A Singleton object `RetrofitBuilder` that provides methods to create instances of Retrofit for
 * different APIs. It ensures that the Retrofit instances are configured correctly with the
 * appropriate base URL, converter factory, and, if needed, a client.
 *
 * This object provides a centralized way to configure and create Retrofit instances, promoting
 * code re-usability and reducing redundancy.
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

    fun buildWeatherApi(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}