package no.hiof.friluftslivcompanionapp.models

import no.hiof.friluftslivcompanionapp.models.enums.DisplayPicture
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage

data class UserPreferences(
    var displayPicture: DisplayPicture = DisplayPicture.DP_DEFAULT,
    var darkModeEnabled: Boolean = false,
    var language: SupportedLanguage = SupportedLanguage.ENGLISH
)
