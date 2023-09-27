package no.hiof.friluftslivcompanionapp

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import kotlin.system.exitProcess

suspend fun main(): Unit = coroutineScope {
    launch {


        val birds = BirdObservations()
        println(birds.getRecentObservationsInOslo("no"))

        exitProcess(0)
    }
}
