import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

val keystorePropertiesFile = rootProject.file("keystore.properties")


val keystoreProperties = Properties()


keystoreProperties.load(FileInputStream(keystorePropertiesFile))


android {
    signingConfigs {
        getByName("debug") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String


        }
    }
    namespace = "no.hiof.friluftslivcompanionapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "no.hiof.friluftslivcompanionapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations += listOf("en", "no")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Get the API keys from the local.properties file
        val properties = Properties().apply {
            load(project.rootProject.file("local.properties").inputStream())
        }

        // Return empty string if properties was not loaded.
        val eBirdKey = properties.getProperty("EBIRD_API_KEY")?.let { "\"$it\"" } ?: "\"\""
        buildConfigField("String", "EBIRD_API_KEY", eBirdKey)

        val weatherKey = properties.getProperty("WEATHER_API_KEY")?.let { "\"$it\"" } ?: "\"\""
        buildConfigField("String", "WEATHER_API_KEY", weatherKey)
    }

    buildTypes {
        debug {
            buildConfigField("String", "EBIRD_API_KEY", "\"${System.getenv("EBIRD_API_KEY")}\"")
            buildConfigField("String", "WEATHER_API_KEY", "\"${System.getenv("WEATHER_API_KEY")}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "EBIRD_API_KEY", "\"${System.getenv("EBIRD_API_KEY")}\"")
            buildConfigField("String", "WEATHER_API_KEY", "\"${System.getenv("WEATHER_API_KEY")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation ("io.coil-kt:coil-compose:1.3.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation ("org.mockito:mockito-core:5.5.0")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.4")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    ksp("com.google.dagger:hilt-compiler:2.47")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.38.1")

    // Navigation Compose - required to inject viewModels into NavHost routes
    implementation("androidx.navigation:navigation-compose:2.7.3")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Google Play - need to have this for google authentication
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Firebase UI Library
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")

    // Google Fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.4.3")

    // Google Play need to have this for google authentication
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.5.0")

    // Google Maps
    implementation("com.google.maps.android:maps-compose:2.14.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.activity:activity-ktx:1.8.0")

    // GSON
    implementation ("com.google.code.gson:gson:2.10.1")
    // Google Places
    implementation("com.google.android.libraries.places:places:3.0.0")

    // Maps Utils
    implementation("com.google.maps.android:android-maps-utils:0.6.2")

    // Material Design 3 - extra (e.g. SecondaryTabRow)
    implementation("androidx.compose.material3:material3:1.2.0-alpha09")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    testImplementation ("org.robolectric:robolectric:4.7.1")

    // Geofire
    implementation("com.firebase:geofire-android-common:3.2.0")

    testImplementation("org.robolectric:robolectric:4.7.1")

}