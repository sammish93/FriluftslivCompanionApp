package no.hiof.friluftslivcompanionapp.data.states

import no.hiof.friluftslivcompanionapp.models.Lifelist

data class LifeListState (
    val lifeList: List<Lifelist>? = null,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false
)