package no.hiof.friluftslivcompanionapp.ui.components

import ListItemComponent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import no.hiof.friluftslivcompanionapp.ui.components.items.CardItem

@Composable
fun ListComponent (items: List<CardItem>){
    LazyColumn{
        items(items){item->
            ListItemComponent(item)
        }
    }

}