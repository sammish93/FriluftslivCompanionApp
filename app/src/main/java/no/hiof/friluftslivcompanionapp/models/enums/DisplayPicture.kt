package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

//TODO Find display pictures.
enum class DisplayPicture(val defaultResolution: Int, val alternativeResolution: Int) {
    DP_DEFAULT(R.drawable.avatar1, R.drawable.ic_launcher_background),
    DP_ONE(R.drawable.avatar2, R.drawable.ic_launcher_background),
    DP_TWO(R.drawable.avatar3, R.drawable.ic_launcher_background),
    DP_THREE(R.drawable.avatar4, R.drawable.ic_launcher_background),
    DP_FOUR(R.drawable.avatar5, R.drawable.ic_launcher_background),
    DP_FIVE(R.drawable.avatar6, R.drawable.ic_launcher_background),
    DP_SIX(R.drawable.avatar7, R.drawable.ic_launcher_background),
    DP_SEVEN(R.drawable.avatar8, R.drawable.ic_launcher_background),
    DP_EIGHT(R.drawable.avatar9, R.drawable.ic_launcher_background),
}