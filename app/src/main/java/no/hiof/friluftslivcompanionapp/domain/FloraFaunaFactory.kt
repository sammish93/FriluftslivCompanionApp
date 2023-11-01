package no.hiof.friluftslivcompanionapp.domain

import com.google.android.gms.maps.model.LatLng
import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.FloraFaunaSighting
import no.hiof.friluftslivcompanionapp.models.Hike
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.Trip
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import no.hiof.friluftslivcompanionapp.models.enums.TripType
import java.time.Duration
import java.time.LocalDate
import java.util.Date

object FloraFaunaFactory {
    //TODO Add validation and test.
    fun createSighting(species: FloraFauna, date: Date, location: Location): FloraFaunaSighting? {
        return FloraFaunaSighting(species = species, date = date, location = location)
    }
}