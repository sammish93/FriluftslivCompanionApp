package no.hiof.friluftslivcompanionapp.models

import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage

data class UserPreferences(
    var darkModeEnabled: Boolean = false,
    var language: SupportedLanguage = SupportedLanguage.ENGLISH
)
