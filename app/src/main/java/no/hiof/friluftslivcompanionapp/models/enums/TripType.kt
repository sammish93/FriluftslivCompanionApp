package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class TripType(var label: Int) {
    HIKE(R.string.trip_type_hike),
    SKI(R.string.trip_type_cross_country_ski),
    CLIMB(R.string.trip_type_climb)
}