package no.hiof.friluftslivcompanionapp.data.network.remote

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

// Reference: https://firebase.google.com/docs/remote-config/get-started?authuser=0&platform=android
/**
 * Object to handle Firebase Remote Config-operations.
 *
 * This object offers functionality to retrieve and activate configuration values from Firebase Remote Config.
 * It also contains a function to retrieve a particular configuration string based on a key.
 *
 * Reference: [Firebase Remote Config documentation](https://firebase.google.com/docs/remote-config/get-started?authuser=0&platform=android)
 */
object FirebaseConfigManager {

    fun fetchConfigSingleton(fetchIntervalInSeconds: Long, onComplete: (Boolean) -> Unit) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = fetchIntervalInSeconds
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf("home_screen_text" to "Default text"))

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            onComplete(task.isSuccessful)
        }
    }

    fun getString(key: String): String {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        return remoteConfig.getString(key)
    }

}