package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations.getInstance(SupportedLanguage.ENGLISH)
    val result = api.getRecentObservations(maxResult = 10)

    if (result is Result.Success)
        println(result.value)

    exitProcess(0)
}
