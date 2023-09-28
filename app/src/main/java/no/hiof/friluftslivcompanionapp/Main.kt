package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations()
    val result = api.getRecentObservationsInOslo("en")

    println(result)
    exitProcess(0)
}
