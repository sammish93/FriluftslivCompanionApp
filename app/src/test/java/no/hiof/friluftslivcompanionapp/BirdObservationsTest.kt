package no.hiof.friluftslivcompanionapp

import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime


class BirdObservationsTest {

    private lateinit var birdObservations: BirdObservations

    @Before
    fun setUp() {
        birdObservations = BirdObservations.getInstance()
    }

    @Test
    fun getRecentObservations_returnsExpectedResult() = runBlocking {

        val expected = Result.Success(listOf(
            Bird(
                speciesName = "Common Murre",
                speciesNameScientific = "Uria aalge",
                number = 16,
                photoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Common_Murre_Uria_aalge.jpg/600px-Common_Murre_Uria_aalge.jpg",
                description = "The common murre or common guillemot (Uria aalge) is a large auk...",
                observationDate = LocalDateTime.of(2023, 9, 30, 16, 37),
                coordinates = Location(lat = 59.904504, lon = 10.753352)
            )
        ))

        val result = birdObservations.getRecentObservations(year=2023, month=9, day=30)
        val observations = if (result is Result.Success) result.value else listOf()

        assertEquals(expected.value[0].speciesName, observations[0].speciesName)
        assertEquals(expected.value[0].speciesNameScientific, observations[0].speciesNameScientific)
        assertEquals(expected.value[0].photoUrl, observations[0].photoUrl)
        assertEquals(expected.value[0].observationDate, observations[0].observationDate)
        assertEquals(expected.value[0].coordinates, observations[0].coordinates)
    }

    // TODO(Fix this test later)
    @Test
    fun getRecentObservations_returnsResultInNorwegian() = runBlocking {

        val expected = Result.Success(listOf(
            Bird(
                speciesName = "lomvi",
                speciesNameScientific = "Uria aalge",
                number = 16,
                photoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/64/Common_Murre_Uria_aalge.jpg/600px-Common_Murre_Uria_aalge.jpg",
                description = """
                    Lomvi eller ringvi (Uria aalge) er en pelagisk dykkende sjøfugl og den største av alkene (Alcini), en monofyletisk gruppe som tilhører familien alkefugler (Alcidae). Arten finnes i tempererte og lavarktiske kyststrøk i nordområdene i Atlanterhavet og Stillehavet.
                    Norsk lomvi hekker først og fremst på Bjørnøya, der det finnes omkring 100 000 par av denne arten. Noen få kolonier hekker også på Spitsbergen, henholdsvis ved Prins Karls Forland og Amsterdamøya.
                """.trimIndent(),
                observationDate = LocalDateTime.of(2023, 9, 30, 16, 37),
                coordinates = Location(lat = 59.904504, lon = 10.753352)
            )
        ))


        val result = birdObservations.getRecentObservations(year=2023, month=9, day=30)
        assertEquals(expected, result)
    }
}