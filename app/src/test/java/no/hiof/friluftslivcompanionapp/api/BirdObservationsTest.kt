package no.hiof.friluftslivcompanionapp.api

import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage


import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.time.LocalDate
import java.time.LocalDateTime


class BirdObservationsTest {

    private lateinit var birdObservations: BirdObservations

    @Before
    fun setUp() {
        birdObservations = mock(BirdObservations::class.java)
    }

    @Test
    fun getRecentObservations_returnsExpectedResult() = runBlocking {

        // Arrange
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
        `when`(birdObservations.getRecentObservations(year = 2023, month = 9, day = 30)).thenReturn(expected)

        // Act
        val result = birdObservations.getRecentObservations(year = 2023, month = 9, day = 30)
        val observations = if (result is Result.Success) result.value else listOf()

        // Assert
        assertEquals(expected.value[0].speciesName, observations[0].speciesName)
        assertEquals(expected.value[0].speciesNameScientific, observations[0].speciesNameScientific)
        assertEquals(expected.value[0].photoUrl, observations[0].photoUrl)
        assertEquals(expected.value[0].observationDate, observations[0].observationDate)
        assertEquals(expected.value[0].coordinates, observations[0].coordinates)
    }

    @Test
    fun getRecentObservations_returnsResultInNorwegian() = runBlocking {

        // Arrange
        val expected = Result.Success(listOf(
            Bird(
                speciesName = "lomvi",
                speciesNameScientific = "Uria aalge",
                number = 16,
                photoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3d/Uria_aalge_-Iceland_-swimming-8.jpg/600px-Uria_aalge_-Iceland_-swimming-8.jpg",
                description = """
                    Lomvi eller ringvi (Uria aalge) er en pelagisk dykkende sjøfugl og den største av alkene (Alcini), en monofyletisk gruppe som tilhører familien alkefugler (Alcidae). Arten finnes i tempererte og lavarktiske kyststrøk i nordområdene i Atlanterhavet og Stillehavet.
                    Norsk lomvi hekker først og fremst på Bjørnøya, der det finnes omkring 100 000 par av denne arten. Noen få kolonier hekker også på Spitsbergen, henholdsvis ved Prins Karls Forland og Amsterdamøya.
                """.trimIndent(),
                observationDate = LocalDateTime.of(2023, 9, 30, 16, 37),
                coordinates = Location(lat = 59.904504, lon = 10.753352)
            )
        ))
        `when`(birdObservations.getRecentObservations(
            languageCode = SupportedLanguage.NORWEGIAN, year = 2023, month = 9, day = 30))
            .thenReturn(expected)

        // Act
        val observations = birdObservations.getRecentObservations(
            languageCode = SupportedLanguage.NORWEGIAN, year = 2023, month = 9, day = 30)
        val result = if (observations is Result.Success) observations.value else listOf()

        // Assert
        assertEquals(expected.value[0].speciesName, result[0].speciesName)
        assertEquals(expected.value[0].speciesNameScientific, result[0].speciesNameScientific)
        assertEquals(expected.value[0].number, result[0].number)
        assertEquals(expected.value[0].photoUrl, result[0].photoUrl)
        assertEquals(expected.value[0].description, result[0].description)
        assertEquals(expected.value[0].observationDate, result[0].observationDate)
        assertEquals(expected.value[0].coordinates, result[0].coordinates)
    }

    @Test
    fun getRecentObservations_returnRightAmountOfBirdObjects() = runBlocking {

        // Arrange
        val birdMock = mock<Bird>()
        val birdList = Result.Success(List(5) { birdMock })

        // Act
        val observation = BirdObservations.getInstance().getRecentObservations(maxResult = 5)
        val result = if (observation is Result.Success) observation.value else listOf()

        // Assert
        assertEquals(birdList.value.size, result.size)
    }

    @Test
    fun getRecentObservation_returnErrorMessageOnNonExistingRegionCode() = runBlocking {

        // Arrange
        val errorMessage = "Error 400 - Invalid request: Please check the region code and try again"
        `when`(birdObservations.getRecentObservations(regionCode = "FSFDf"))
            .thenReturn(Result.Failure(errorMessage))

        // Act
        val observations = birdObservations.getRecentObservations(regionCode = "FSFDf")
        val result = if (observations is Result.Failure) observations.message else ""

        // Assert
        assertEquals(errorMessage, result)
    }

    @Test
    fun getRecentObservations_returnsEmptyListOnInvalidDate() = runBlocking {

        // Arrange
        val emptyList = emptyList<Bird>()

        // Act
        val observation = BirdObservations.getInstance().getRecentObservations(year = 2025, month = 5, day = 10)
        val result = if (observation is Result.Success) observation.value else null

        // Assert
        assertEquals(emptyList, result)

    }

    @Test
    fun getRecentObservations_returnsResultsBasedOnTheCurrentDayWithDefaultParameter() = runBlocking {

        // Arrange
        val currentDate = LocalDate.now()

        // Act
        val observations = BirdObservations.getInstance().getRecentObservations()
        val result = if (observations is Result.Success) observations.value else emptyList()

        // Assert
        assertEquals(currentDate, result[0].observationDate.toLocalDate())
    }

    @Test
    fun getObservationsBetweenDates_returnsSuccessOnValidParameters() = runBlocking {

        // Arrange
        val start = LocalDate.of(2023, 9, 13)
        val end = LocalDate.of(2023, 9, 15)
        val observations = BirdObservations.getInstance()

        // Act
        val result = observations.getObservationsBetweenDates(startDate = start, endDate = end)
        val data = if (result is Result.Success) result.value else null

        // Assert
        assertTrue(result is Result.Success)
        assertFalse(result is Result.Failure)
        assertNotNull(data)
    }

    @Test
    fun getObservationsBetweenDates_returnsObservationsBetweenSpecifiedDate() = runBlocking {

        // Arrange
        val start = LocalDate.of(2023, 9, 13)
        val end = LocalDate.of(2023, 9, 15)
        val observations = BirdObservations.getInstance()

        // Act
        val result = observations.getObservationsBetweenDates(
            startDate = start,
            endDate = end,
            maxResult = 5
        )
        val data = if (result is Result.Success) result.value else emptyList()

        // Assert
        data.forEach { bird ->
            assertTrue(bird.observationDate.toLocalDate() >= start)
            assertTrue(bird.observationDate.toLocalDate() <= end)
        }
    }
}