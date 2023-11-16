package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.enums.FloraFaunaSubclass
import org.junit.Assert.*
import org.junit.Test
import no.hiof.friluftslivcompanionapp.models.Location
import org.mockito.Mockito

class FloraFaunaMapperTest{
    @Test
    fun testMapClassToEnum_Bird_ReturnsBirdEnum() {
        val bird = Bird("Sparrow", "Passeridae", 100,
            "A small bird", "sparrow.jpg", Location(40.0, 20.0))

        val result = FloraFaunaMapper.mapClassToEnum(bird)

        assertEquals(FloraFaunaSubclass.BIRD, result)
    }

    @Test
    fun testMapClassToEnum_UnknownSpecies_ReturnsNull() {
        val unknownSpecies = Mockito.mock(FloraFauna::class.java)

        Mockito.`when`(unknownSpecies.speciesName).thenReturn("Mocked species")
        Mockito.`when`(unknownSpecies.speciesNameScientific).thenReturn("Mocked scientific name")
        Mockito.`when`(unknownSpecies.description).thenReturn("Mocked description")
        Mockito.`when`(unknownSpecies.photoUrl).thenReturn("Mocked photo URL")

        val result = FloraFaunaMapper.mapClassToEnum(unknownSpecies)

        assertNull(result)
    }
}

//Jain(2018)