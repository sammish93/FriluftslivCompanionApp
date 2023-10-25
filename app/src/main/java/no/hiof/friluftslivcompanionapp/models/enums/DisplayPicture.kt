package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

//TODO Find display pictures.
enum class DisplayPicture(val defaultResolution: Int, val alternativeResolution: Int) {
    DP_DEFAULT(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_ONE(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_TWO(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_THREE(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_FOUR(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_FIVE(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_SIX(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_SEVEN(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
    DP_EIGHT(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background),
}