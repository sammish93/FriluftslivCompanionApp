package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations.getInstance()
    val result = api.getRecentObservations(year = 2024, month = 5, day = 17)

    if (result is Result.Success)
        println(result.value)
    else
        if (result is Result.Failure)
            println(result.message)
    exitProcess(0)
}
