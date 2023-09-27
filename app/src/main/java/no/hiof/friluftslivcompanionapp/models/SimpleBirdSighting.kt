package no.hiof.friluftslivcompanionapp.models

data class SimpleBirdSighting(
    val speciesCode: String,
    val comName: String,
    val sciName: String,
    val obsDt: String,
    val howMany: Int,
    val lat: Double,
    val lng: Double
)

