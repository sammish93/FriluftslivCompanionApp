package no.hiof.friluftslivcompanionapp.models

import com.google.type.DateTime

// Datetime vs Date - Datetime allows a user to do multiple trips on the same day while still
// using Map.
data class TripActivity(val activity: Map<DateTime, Trip>)
