package no.hiof.friluftslivcompanionapp.models.api

// Class used to get the name and scientific name from the eBird API.
data class SimpleBirdSighting(
    val comName: String,
    val sciName: String,
    val howMany: Int,
    val obsDt: String,
    var lat: Double,
    var lng: Double
)

