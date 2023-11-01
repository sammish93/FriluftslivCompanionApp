package no.hiof.friluftslivcompanionapp.data.states

import no.hiof.friluftslivcompanionapp.models.SpeciesInfo
import no.hiof.friluftslivcompanionapp.models.FloraFauna

data class FloraFaunaState(
    val speciesResults: List<FloraFauna> = emptyList(),
    val selectedSpeciesInfo: SpeciesInfo? = null,
    val isLoading: Boolean = false
)
