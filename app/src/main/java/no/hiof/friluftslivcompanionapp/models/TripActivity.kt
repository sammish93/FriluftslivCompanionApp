package no.hiof.friluftslivcompanionapp.models

import java.util.Date

// Datetime vs Date - Datetime seems to cause compile errors with kotlin. Date supports time alse.
data class TripActivity(val tripActivity: Map<Date, Trip>)
