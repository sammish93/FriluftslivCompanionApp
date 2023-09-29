package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.data.api.EBirdApi
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage

class BirdObservations {

    suspend fun getRecentObservationsInOslo(language: SupportedLanguage): List<Bird>? {
        return EBirdApi().getRecentObservations(language.code)
    }
}