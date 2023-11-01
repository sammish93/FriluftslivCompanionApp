package no.hiof.friluftslivcompanionapp.models

import no.hiof.friluftslivcompanionapp.models.enums.DisplayPicture
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage

data class UserPreferences(
    var displayPicture: DisplayPicture = DisplayPicture.DP_DEFAULT,
    var darkModeEnabled: Boolean = false,
    var language: SupportedLanguage = SupportedLanguage.ENGLISH
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "displayPicture" to displayPicture.name,
            "darkModeEnabled" to darkModeEnabled,
            "language" to language.name )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): UserPreferences {
            return UserPreferences(
                displayPicture = DisplayPicture.valueOf(map["displayPicture"] as String),
                darkModeEnabled = map["darkModeEnabled"] as Boolean,
                language = SupportedLanguage.valueOf(map["language"] as String)
            )
        }
    }
}
