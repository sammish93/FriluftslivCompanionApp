package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.api.WeatherApi
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.WeatherDeserialiser
import no.hiof.friluftslivcompanionapp.models.Weather

suspend fun main() {
     val api = WeatherDeserialiser.getInstance()


    suspend fun getCurrentWeather() {

        val result = api.getCurrentWeather(41.389, 2.159)
        val weather = if (result is Result.Success) result.value else null

        System.out.println(weather)
    }

    getCurrentWeather()
}