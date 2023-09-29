package no.hiof.friluftslivcompanionapp.data.network
import no.hiof.friluftslivcompanionapp.BuildConfig
import okhttp3.OkHttpClient

/**
 * A Singleton object `HttpClient` that holds an instance of OkHttpClient.
 * OkHttpClient is designed to be shared across the application, and it’s efficient to share an instance
 * to use for all HTTP calls. This is because the shared instance will keep track of all ongoing requests
 * and reuse connections and threads, reducing latency and memory usage.
 *
 * The instance of OkHttpClient in this object is configured with an interceptor that adds the eBird API key
 * to all outgoing requests. This is necessary for making authenticated requests to the eBird API.
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

