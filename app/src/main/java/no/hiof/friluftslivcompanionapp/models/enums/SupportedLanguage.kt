package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class SupportedLanguage(val code: String, val nameLocalized: Int) {
    ENGLISH("en", R.string.lang_english),
    NORWEGIAN("no", R.string.lang_norwegian)
}