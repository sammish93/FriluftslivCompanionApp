package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import java.time.LocalDate
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations()
    val start = LocalDate.of(2023, 8, 13)
    val end = LocalDate.of(2023, 8, 15)
    val result = api.getObservationsBetweenDates(start, end, maxResult = 1)

    println(result)
    exitProcess(0)
}
