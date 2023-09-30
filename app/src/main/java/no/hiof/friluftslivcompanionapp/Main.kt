package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations()
    val result = api.getRecentObservationsInOslo(regionCode = "NO", language = SupportedLanguage.NORWEGIAN, maxResult = 10)
    val birdList = if (result is Result.Success) result.value else null

    println(birdList)
    exitProcess(0)
}
