package no.hiof.friluftslivcompanionapp.data.messaging.backgroundtask

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import no.hiof.friluftslivcompanionapp.data.messaging.NotificationHelper
import no.hiof.friluftslivcompanionapp.domain.WeatherDeserialiser
import no.hiof.friluftslivcompanionapp.models.Weather
import no.hiof.friluftslivcompanionapp.models.enums.WeatherTriggers
import no.hiof.friluftslivcompanionapp.models.enums.WeatherType
import no.hiof.friluftslivcompanionapp.R

class CheckWeatherJob(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    // Call weather api and check if we need to send out notification.
    override suspend fun doWork(): Result {
        val weatherApi = WeatherDeserialiser.getInstance()
        return when (val call = weatherApi.getWeatherForecast(59.434031, 10.657711)) {

            is no.hiof.friluftslivcompanionapp.data.network.Result.Success -> {
                val weather = call.value.forecast[0]
                Log.d("CheckWeatherJob", "Successfully fetched weather data: $weather")
                Log.d("CheckWeatherJob", "Checking for extreme weather...")
                checkForExtremeWeather(weather)
                Result.success()
            }

            is no.hiof.friluftslivcompanionapp.data.network.Result.Failure -> {
                Log.e("CheckWeatherJob", "Failed to fetch weather data: ${call.message}")
                Result.failure()
            }
        }
    }

    private fun checkForExtremeWeather(weather: Weather) {

        when (weather.weatherType) {
            WeatherType.THUNDERSTORM -> {
                Log.d("CheckForExtremeWeather", "Sending notification for thunder storm.")
                sendNotification(
                    "Thunder Storm",
                    R.drawable.thunder_storm,
                    "Watch out there is a thunder storm in your area!"
                )
            }
            WeatherType.SNOW -> {
                Log.d("CheckForExtremeWeather", "Sending notification for snow.")
                sendNotification(
                    "Snow",
                    R.drawable.snow,
                    "It is snow in your area, be careful when driving!"
                )
            }
            else -> {
                if (weather.windSpeed > WeatherTriggers.WIND_IN_METER.threshold) {
                    Log.d("CheckForExtremeWeather", "Sending notification for strong wind.")
                    sendNotification(
                        "Windy",
                        R.drawable.windy,
                        "There is a lot of wind in your area, stay inside!"
                        )
                }
                else {
                    sendNotification(
                        "Nice Weather",
                        R.drawable.nice_weather,
                        "It is nice weather today with little wind :)"
                    )
                    Log.d("CheckForExtremeWeather", "No extreme weather at the moment.")
                }
            }
        }
    }

    private fun sendNotification(title: String, icon: Int, body: String) {
        NotificationHelper.showMessage(applicationContext, title, icon, body)
    }
}