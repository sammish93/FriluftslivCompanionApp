package no.hiof.friluftslivcompanionapp.domain

import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.FloraFaunaSighting
import no.hiof.friluftslivcompanionapp.models.Location
import java.util.Date

object FloraFaunaFactory {
    fun createSighting(species: FloraFauna, date: Date, location: Location): FloraFaunaSighting? {
        if (date > Date()) {
            println("Error: The target date cannot be in the future.")
            return null
        }

        // Validating the location to ensure it is valid
        if (!isLocationValid(location)) {
            println("Error: Invalid location.")
            return null
        }

        val geohash = GeoFireUtils.getGeoHashForLocation(GeoLocation(location.lat, location.lon))

        location.geoHash = geohash

        return FloraFaunaSighting(species = species, date = date, location = location)
    }

    private fun isLocationValid(location: Location): Boolean {
        val validLatitudeRange = 35.0..71.0 // Valid latitudes for Europe
        val validLongitudeRange = -10.0..40.0 // Valid longitudes for Europe


        val isLatitudeValid = location.lat in validLatitudeRange
        val isLongitudeValid = location.lon in validLongitudeRange

        return isLatitudeValid && isLongitudeValid
    }
}

//If we need for the whole world, use this:
//val validLatitudeRange = -90.0..90.0
//val validLongitudeRange = -180.0..180.0