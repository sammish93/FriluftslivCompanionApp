package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations()
    val result = api.getRecentObservationsInOslo(language = SupportedLanguage.NORWEGIAN)
    val birdList = if (result is Result.Success) result.value else null

    println(birdList)
    exitProcess(0)
}
