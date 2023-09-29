package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations()
    val result = api.getRecentObservationsInOslo(SupportedLanguage.NORWEGIAN)

    println(result)
    exitProcess(0)
}
