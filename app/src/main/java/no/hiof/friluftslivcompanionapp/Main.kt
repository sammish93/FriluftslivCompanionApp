package no.hiof.friluftslivcompanionapp

import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.data.repositories.BirdRepository

fun main() = runBlocking {

    val repository = BirdRepository()
    val birds = repository.getRecentObservations()
    println(birds)
}