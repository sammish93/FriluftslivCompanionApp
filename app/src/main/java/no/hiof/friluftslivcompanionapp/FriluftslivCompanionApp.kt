package no.hiof.friluftslivcompanionapp

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
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
        setUpImmediateWorkManager()
        setUpWorkManager()
    }

    // Job that will run immediately when the app starts.
    private fun setUpImmediateWorkManager() {
        val immediateWorkRequest = OneTimeWorkRequestBuilder<CheckWeatherJob>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()

        WorkManager.getInstance(applicationContext).enqueue(immediateWorkRequest)
    }

    // Periodic job that will run every 6 hour´s
    private fun setUpWorkManager() {
        val weatherCheckRequest = getWorkRequestBuilder()
        enqueueJob(this, weatherCheckRequest)
    }

    private fun getWorkRequestBuilder(hourlyInterval: Long=6): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<CheckWeatherJob>(hourlyInterval, TimeUnit.HOURS)

            // Delay so it don't overlap with immediate job.
            .setInitialDelay(1, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
    }

    private fun enqueueJob(context: Context, job: PeriodicWorkRequest) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("CheckWeatherWork", ExistingPeriodicWorkPolicy.KEEP, job)
    }

    // Function used to cancel the periodic job.
    fun cancelWeatherCheckJob() {
        WorkManager.getInstance(applicationContext).cancelUniqueWork("CheckWeatherWork")
    }
}