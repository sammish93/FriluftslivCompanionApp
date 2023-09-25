package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class DisplayPicture(val resourcePaths: Map<String, Int>) {
    DP_DEFAULT(
        mapOf(
            "defaultResolution" to R.drawable.ic_launcher_foreground,
            "alternateResolution" to R.drawable.ic_launcher_background
        )
    ),
    DP_ONE(
        mapOf(
            "defaultResolution" to R.drawable.ic_launcher_foreground,
            "alternateResolution" to R.drawable.ic_launcher_background
        )
    ),
    DP_TWO(
        mapOf(
            "defaultResolution" to R.drawable.ic_launcher_foreground,
            "alternateResolution" to R.drawable.ic_launcher_background
        )
    )
}