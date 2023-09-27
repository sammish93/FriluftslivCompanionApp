package no.hiof.friluftslivcompanionapp.data.network
import no.hiof.friluftslivcompanionapp.BuildConfig
import okhttp3.OkHttpClient

/**
 * A Singleton object of the OkHttpClient. It performs best when we only use one
 * instance of this class, and reuse it for all our HTTP calls.
 */
object HttpClient {
    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("x-ebirdapitoken", BuildConfig.EBIRD_API_KEY)
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }
}

