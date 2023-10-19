package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class Screen(val route: String, val navBarLabel: Int) {
    HOME("home", R.string.navigation_home),
    FLORA_FAUNA("floraFauna", R.string.navigation_wildlife),
    FLORA_FAUNA_ADDITIONAL_INFO(
        "floraFaunaAdditionalInfo",
        R.string.navigation_additional_information
    ),
    FLORA_FAUNA_ADD("floraFauna", R.string.navigation_add_sighting),
    FLORA_FAUNA_SEARCH_LOCATION("floraFauna", R.string.navigation_search),
    FLORA_FAUNA_SEARCH_SPECIES("floraFauna", R.string.navigation_undefined),
    FLORA_FAUNA_SEARCH_RESULT("floraFauna", R.string.navigation_search_results),
    PROFILE("profile", R.string.navigation_profile),
    PROFILE_SETTINGS("profile", R.string.navigation_settings),
    WEATHER("weather", R.string.navigation_weather),
    WEATHER_SEARCH("weather", R.string.navigation_search),
    WEATHER_SEARCH_RESULT("weather", R.string.navigation_search_results),
    TRIPS("trips", R.string.navigation_trips),
    TRIPS_ADDITIONAL_INFO("trips", R.string.navigation_additional_information),
    TRIPS_ADD("trips", R.string.navigation_add_trip),
    TRIPS_CREATE("trips", R.string.navigation_create_trip),
    TRIPS_RECENT_ACTIVITY("trips", R.string.navigation_recent_activity),
    LOGIN("login", R.string.navigation_undefined)
}