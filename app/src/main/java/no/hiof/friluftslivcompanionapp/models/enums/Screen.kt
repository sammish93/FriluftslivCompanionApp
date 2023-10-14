package no.hiof.friluftslivcompanionapp.models.enums

enum class Screen(val route: String, val navBarLabelEn: String, val navBarLabelNo: String) {
    HOME ("home", "Home", "Hjem"),
    FLORA_FAUNA ("floraFauna", "Wildlife", "Arter"),
    FLORA_FAUNA_ADDITIONAL_INFO ("floraFaunaAdditionalInfo", "Additional Info", "Tilleggsinformasjon"),
    FLORA_FAUNA_ADD ("floraFauna", "UNDEFINED", "UNDEFINED"),
    FLORA_FAUNA_SEARCH_LOCATION ("floraFauna", "UNDEFINED", "UNDEFINED"),
    FLORA_FAUNA_SEARCH_SPECIES ("floraFauna", "UNDEFINED", "UNDEFINED"),
    FLORA_FAUNA_SEARCH_RESULT ("floraFauna", "UNDEFINED", "UNDEFINED"),
    PROFILE ("profile", "Profile", "Profil"),
    PROFILE_SETTINGS ("profile", "UNDEFINED", "UNDEFINED"),
    WEATHER ("weather", "Weather", "Vær"),
    WEATHER_SEARCH ("weather",  "UNDEFINED", "UNDEFINED"),
    WEATHER_SEARCH_RESULT ("weather",  "UNDEFINED", "UNDEFINED"),
    TRIPS ("trips",  "Trips", "Turer"),
    TRIPS_ADDITIONAL_INFO ("trips", "UNDEFINED", "UNDEFINED"),
    TRIPS_ADD ("trips", "UNDEFINED", "UNDEFINED"),
    TRIPS_CREATE ("trips", "UNDEFINED", "UNDEFINED"),
    TRIPS_RECENT_ACTIVITY ("trips", "UNDEFINED", "UNDEFINED"),
    LOGIN ("login", "UNDEFINED", "UNDEFINED")
}