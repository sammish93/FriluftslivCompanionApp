package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.api.WeatherApi

suspend fun main() {
    var thing = WeatherApi()
    var thisOtherThing = thing.getWeatherInfo(41.389, 2.159)

    System.out.println(thisOtherThing)
}