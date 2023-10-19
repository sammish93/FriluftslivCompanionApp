package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.api.WeatherApi
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.domain.DateFormatter
import no.hiof.friluftslivcompanionapp.domain.Geocoding
import no.hiof.friluftslivcompanionapp.domain.WeatherDeserialiser
import no.hiof.friluftslivcompanionapp.models.Weather
import java.io.Console

suspend fun main() {
    //System.out.println(Geocoding.getRegionCode(62.6259, 7.0867))
    val latlng = "62.6259,7.0867"
    val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY

    val componentsFilter = "country:NO"
    val response = Geocoding.geocodingApiService.getAddressFromCoordinates(latlng, apiKey,componentsFilter).execute()
    System.out.println(response)
}