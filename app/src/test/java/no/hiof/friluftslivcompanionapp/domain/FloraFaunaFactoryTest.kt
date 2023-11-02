package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.Location
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Date

class FloraFaunaFactoryTest{

    @Test
    fun testCreateSighting_DateInFuture_ReturnsNull() {
        val futureDate = Date(System.currentTimeMillis() + 1000000)
        val validLocation = Location(60.0, 30.0)

        val floraFaunaStub = mock(FloraFauna::class.java)

        `when`(floraFaunaStub.speciesName).thenReturn("Mocked species")
        `when`(floraFaunaStub.speciesNameScientific).thenReturn("Mocked scientific name")
        `when`(floraFaunaStub.description).thenReturn("Mocked description")
        `when`(floraFaunaStub.photoUrl).thenReturn("Mocked photo URL")

        val result = FloraFaunaFactory.createSighting(floraFaunaStub, futureDate, validLocation)
        assertNull("Expected null for a sighting with a date in the future", result)
    }

    @Test
    fun testCreateSighting_CurrentDate_ValidLocation_ValidSpecies_ReturnsSightingObject() {
        val currentDate = Date()
        val validLocation = Location(40.0, 20.0)
        val validSpecies = mock(FloraFauna::class.java)

        `when`(validSpecies.speciesName).thenReturn("Mocked species")
        `when`(validSpecies.speciesNameScientific).thenReturn("Mocked scientific name")
        `when`(validSpecies.description).thenReturn("Mocked description")
        `when`(validSpecies.photoUrl).thenReturn("Mocked photo URL")

        val result = FloraFaunaFactory.createSighting(validSpecies, currentDate, validLocation)
        assertNotNull("Expected a sighting for the current date " +
                "and valid location and valid species", result)
    }


    @Test
    fun testCreateSighting_InvalidLocation_ReturnsNull() {
        val currentDate = Date()
        val invalidLocation = Location(80.0, 50.0)

        val validSpecies = mock(FloraFauna::class.java)

        `when`(validSpecies.speciesName).thenReturn("Mocked species")
        `when`(validSpecies.speciesNameScientific).thenReturn("Mocked scientific name")
        `when`(validSpecies.description).thenReturn("Mocked description")
        `when`(validSpecies.photoUrl).thenReturn("Mocked photo URL")

        val result = FloraFaunaFactory.createSighting(validSpecies, currentDate, invalidLocation)
        assertNull("Expected null for an observation with an invalid location", result)
    }
}