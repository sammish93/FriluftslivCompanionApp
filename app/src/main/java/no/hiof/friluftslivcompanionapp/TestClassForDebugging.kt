package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.api.WeatherApi
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.WeatherDeserialiser

suspend fun main() {
    var thing = WeatherApi()
    var thisOtherThing = thing.getWeatherInfo(41.389, 2.159)
    var today = WeatherDeserialiser.getInstance().getCurrentWeather(41.389, 2.159)
    var forecast = WeatherDeserialiser.getInstance().getWeatherForecast(41.389, 2.159)


    //var thingy = DateFormatter.formatFromUnixTimestamp("1696496593")
    System.out.println(forecast)
    System.out.println(today)
}