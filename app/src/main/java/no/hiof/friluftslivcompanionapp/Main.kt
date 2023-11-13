package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.WeatherDeserialiser
import kotlin.system.exitProcess

suspend fun main() {

    val weatherApi = WeatherDeserialiser.getInstance()

    val call = weatherApi.getWeatherForecast(59.434031, 10.657711)

    val result = if (call is Result.Success) call.value else null

    if (result != null) {
        println(result.forecast[0].windSpeed)
    }
    exitProcess(0)
}