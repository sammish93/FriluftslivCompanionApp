package no.hiof.friluftslivcompanionapp

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import no.hiof.friluftslivcompanionapp.data.messaging.backgroundtask.CheckWeatherJob
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class FriluftslivCompanionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setUpWorkManager()
    }

    private fun setUpWorkManager() {
        // Runs the job every 6 hour = 4 times a day.
        val weatherCheckRequest = getWorkRequestBuilder()
        enqueueJob(this, weatherCheckRequest)
    }

    private fun getWorkRequestBuilder(hourlyInterval: Long=6): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<CheckWeatherJob>(hourlyInterval, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    }

    private fun enqueueJob(context: Context, job: PeriodicWorkRequest) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("CheckWeatherWork", ExistingPeriodicWorkPolicy.KEEP, job)
    }

}