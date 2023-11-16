package no.hiof.friluftslivcompanionapp.domain

import kotlinx.coroutines.runBlocking
import no.hiof.friluftslivcompanionapp.models.enums.WeatherType


import org.junit.Assert.*
import org.junit.Test


class WeatherIconMapperTest {
    @Test
    fun mapIconWithDayString_returnsExpectedResult() = runBlocking {

        val string = "50d"

        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        assertEquals(WeatherType.MIST, iconAsEnum)
    }

    @Test
    fun mapIconWithNightString_returnsExpectedResult() = runBlocking {

        val string = "50n"

        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        assertEquals(WeatherType.MIST, iconAsEnum)
    }

    @Test
    fun mapIconWithAllPossibleDayStrings_returnsExpectedResult() = runBlocking {

        val strings = listOf<String>("01d", "02d", "03d", "04d", "09d", "10d", "11d", "13d", "50d")
        val weatherType = listOf<WeatherType>(
            WeatherType.CLEAR_SKY,
            WeatherType.FEW_CLOUDS,
            WeatherType.SCATTERED_CLOUDS,
            WeatherType.BROKEN_CLOUDS,
            WeatherType.SHOWER_RAIN,
            WeatherType.RAIN,
            WeatherType.THUNDERSTORM,
            WeatherType.SNOW,
            WeatherType.MIST
        )


        for (i in strings.indices) {
            assertEquals(weatherType[i], WeatherIconMapper.mapIconToEnum(strings[i]))
        }

    }

    @Test
    fun mapIconWithAllPossibleNightStrings_returnsExpectedResult() = runBlocking {

        val strings = listOf<String>("01n", "02n", "03n", "04n", "09n", "10n", "11n", "13n", "50n")
        val weatherType = listOf<WeatherType>(
            WeatherType.CLEAR_SKY,
            WeatherType.FEW_CLOUDS,
            WeatherType.SCATTERED_CLOUDS,
            WeatherType.BROKEN_CLOUDS,
            WeatherType.SHOWER_RAIN,
            WeatherType.RAIN,
            WeatherType.THUNDERSTORM,
            WeatherType.SNOW,
            WeatherType.MIST
        )

        for (i in strings.indices) {
            assertEquals(weatherType[i], WeatherIconMapper.mapIconToEnum(strings[i]))
        }

    }

    @Test
    fun mapIconWithAllPossibleDayStrings_returnsExpectedResultAsDrawableResource() = runBlocking {

        val strings = listOf<String>("01d", "02d", "03d", "04d", "09d", "10d", "11d", "13d", "50d")
        val weatherType = listOf<Int>(
            WeatherType.CLEAR_SKY.resourcePath,
            WeatherType.FEW_CLOUDS.resourcePath,
            WeatherType.SCATTERED_CLOUDS.resourcePath,
            WeatherType.BROKEN_CLOUDS.resourcePath,
            WeatherType.SHOWER_RAIN.resourcePath,
            WeatherType.RAIN.resourcePath,
            WeatherType.THUNDERSTORM.resourcePath,
            WeatherType.SNOW.resourcePath,
            WeatherType.MIST.resourcePath
        )


        for (i in strings.indices) {
            assertEquals(weatherType[i], WeatherIconMapper.mapIconToDrawableResource(strings[i]))
        }

    }

    @Test
    fun mapIconWithEmptyString_returnsDefaultValueAsResult() = runBlocking {

        val string = ""

        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        assertEquals(WeatherType.CLEAR_SKY, iconAsEnum)
    }

    @Test
    fun mapIconWithIncorrectString_returnsDefaultValueAsResult() = runBlocking {

        val string = "abcdefg123"

        val iconAsEnum = WeatherIconMapper.mapIconToEnum(string)

        assertEquals(WeatherType.CLEAR_SKY, iconAsEnum)
    }
}