package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.api.WeatherApi

suspend fun main() {
    var thing = WeatherApi()
    var thisOtherThing = thing.getWeatherInfo(62.6259, 7.0867)

    System.out.println(thisOtherThing)
}