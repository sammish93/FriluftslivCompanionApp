package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.R
import kotlin.random.Random

object NameFormatter {

    /**
     * This function can be used when an anonymous user is displayed in the app - to avoid
     * 'DefaultUsername' being printed.
     * @return Returns a stringResource location of a random common name of an Animal.
     */
    fun getRandomName(): Int {
        val animals = listOf(
            R.string.username_hedgehog,
            R.string.username_squirrel,
            R.string.username_polecat,
            R.string.username_mole,
            R.string.username_badger,
            R.string.username_fox,
            R.string.username_magpie,
            R.string.username_mouse,
            R.string.username_robin,
            R.string.username_adder,
            R.string.username_deer,
            R.string.username_moose)
        val randomIndex = Random.nextInt(animals.size)
        return animals[randomIndex]
    }
}