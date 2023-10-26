package no.hiof.friluftslivcompanionapp.models.enums

import no.hiof.friluftslivcompanionapp.R

enum class FloraFaunaSubclass(val label: Int, isEnabled: Boolean) {
    BIRD(R.string.flora_fauna_subclass_bird, true),
    MAMMAL(R.string.flora_fauna_subclass_mammal, false),
    INSECT(R.string.flora_fauna_subclass_insect, false),
    FISH(R.string.flora_fauna_subclass_fish, false),
    PLANT(R.string.flora_fauna_subclass_plant, false),
    FUNGI(R.string.flora_fauna_subclass_fungi, false),
    EXTRATERRESTRIAL(R.string.flora_fauna_subclass_extraterrestrial, false)
}