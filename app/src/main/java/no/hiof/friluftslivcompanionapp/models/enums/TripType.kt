package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class TripType(var label: Int, var isEnabled: Boolean) {
    HIKE(R.string.trip_type_hike, true),
    SKI(R.string.trip_type_cross_country_ski, false),
    CLIMB(R.string.trip_type_climb, false)
}