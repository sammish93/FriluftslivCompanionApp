package no.hiof.friluftslivcompanionapp.data.CardItem;

import androidx.annotation.DrawableRes
import no.hiof.friluftslivcompanionapp.R

data class CardItem(
        @DrawableRes val imageResourceId: Int,
        val title: String )

val cardItems = listOf(
        CardItem(R.drawable.ic_launcher_foreground, title = "Hike 1",),
        CardItem(R.drawable.ic_launcher_foreground, title = "Hike 2",)
)