package no.hiof.friluftslivcompanionapp.data.states

import com.google.android.gms.maps.model.LatLng


data class PlaceInfoState(
    val city: String?,
    val county: String?,
    val country: String?,
    val coordinates: LatLng?,
)
