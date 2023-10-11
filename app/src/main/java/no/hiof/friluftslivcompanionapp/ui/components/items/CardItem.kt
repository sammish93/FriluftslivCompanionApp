package no.hiof.friluftslivcompanionapp.ui.components.items;

import androidx.annotation.DrawableRes
import no.hiof.friluftslivcompanionapp.R

data class CardItem(
        @DrawableRes val imageResourceId: Int,
        val title: String )

val cardItems = listOf(
        CardItem(R.drawable.ic_launcher_foreground, title = "Hike 1"),
        CardItem(R.drawable.ic_launcher_foreground, title = "Hike 2"),
        CardItem(R.drawable.ic_launcher_foreground, title = "Hike 3"),
        CardItem(R.drawable.ic_launcher_foreground, title = "Hike 4")
)


