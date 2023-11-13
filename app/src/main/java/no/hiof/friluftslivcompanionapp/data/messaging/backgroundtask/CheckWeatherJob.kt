package no.hiof.friluftslivcompanionapp.data.messaging.backgroundtask

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class CheckWeatherJob(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}