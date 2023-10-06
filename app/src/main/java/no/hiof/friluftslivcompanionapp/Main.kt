package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations.getInstance()
    val obs = api.getRecentObservations(year=2023, month=9, day=30)

    val result = if (obs is Result.Success) obs.value else emptyList()

    println(result)
    exitProcess(0)

}