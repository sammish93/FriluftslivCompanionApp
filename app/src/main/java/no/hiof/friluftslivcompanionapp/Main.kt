package no.hiof.friluftslivcompanionapp

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.data.repositories.BirdRepository
import kotlin.system.exitProcess

suspend fun main(): Unit = coroutineScope {
    launch {
        val repository = BirdRepository()
        val birds = repository.getRecentObservations()
        println(birds)

        exitProcess(0)
    }
}
