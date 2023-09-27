package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.data.api.EBirdApi
import no.hiof.friluftslivcompanionapp.models.Bird

class BirdObservations {

    suspend fun getRecentObservationsInOslo(language: String): List<Bird>? {
        return EBirdApi().getRecentObservations(language)
    }
}