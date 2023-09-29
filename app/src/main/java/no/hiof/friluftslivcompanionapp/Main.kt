package no.hiof.friluftslivcompanionapp

import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import kotlin.system.exitProcess

suspend fun main() {

    val api = BirdObservations()
    val result: List<Bird>? = api.getRecentObservationsInOslo(SupportedLanguage.ENGLISH)

    val imageUrl = api.processBirdList(result) { url -> url.speciesName ?: "No URL"}

    println(imageUrl)
    exitProcess(0)
}
