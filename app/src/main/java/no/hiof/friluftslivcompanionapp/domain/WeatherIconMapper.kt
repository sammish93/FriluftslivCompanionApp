package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.models.enums.WeatherType

/**
 * This class is used to take a string input from an OpenWeatherMap API request ("icon" key) and
 * return an enum summarising the weather. This enum can then be used to fetch a drawable icon.
 */
object WeatherIconMapper {
    fun mapIconToEnum(iconString: String) : WeatherType {

        val isStringValid = iconString.isNotEmpty()
        var firstTwoCharacters = "01"

        if (isStringValid) {
            firstTwoCharacters = iconString.substring(0, 2)
        }

        val icon : WeatherType = when (firstTwoCharacters) {
            "01" -> WeatherType.CLEAR_SKY
            "02" -> WeatherType.FEW_CLOUDS
            "03" -> WeatherType.SCATTERED_CLOUDS
            "04" -> WeatherType.BROKEN_CLOUDS
            "09" -> WeatherType.SHOWER_RAIN
            "10" -> WeatherType.RAIN
            "11" -> WeatherType.THUNDERSTORM
            "13" -> WeatherType.SNOW
            "50" -> WeatherType.MIST
            else -> WeatherType.CLEAR_SKY
        }

        return icon
    }

    fun mapIconToDrawableResource(iconString: String) : Int {

        val isStringValid = iconString.isNotEmpty()
        var firstTwoCharacters = "01"

        if (isStringValid) {
            firstTwoCharacters = iconString.substring(0, 2)
        }

        val icon : Int = when (firstTwoCharacters) {
            "01" -> WeatherType.CLEAR_SKY.resourcePath
            "02" -> WeatherType.FEW_CLOUDS.resourcePath
            "03" -> WeatherType.SCATTERED_CLOUDS.resourcePath
            "04" -> WeatherType.BROKEN_CLOUDS.resourcePath
            "09" -> WeatherType.SHOWER_RAIN.resourcePath
            "10" -> WeatherType.RAIN.resourcePath
            "11" -> WeatherType.THUNDERSTORM.resourcePath
            "13" -> WeatherType.SNOW.resourcePath
            "50" -> WeatherType.MIST.resourcePath
            else -> WeatherType.CLEAR_SKY.resourcePath
        }

        return icon
    }
}