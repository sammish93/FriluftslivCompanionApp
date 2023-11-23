package no.hiof.friluftslivcompanionapp.data.states

import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.enums.DefaultLocation
import java.util.Date

data class FloraFaunaState(
    val speciesResults: List<FloraFauna> = emptyList(),
    val selectedSpecies: FloraFauna? = null,
    val sightingDate: Date = Date(),
    val sightingLocation: Location = Location(DefaultLocation.OSLO.lat, DefaultLocation.OSLO.lon),
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
)
