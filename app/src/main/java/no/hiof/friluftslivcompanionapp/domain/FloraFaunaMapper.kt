package no.hiof.friluftslivcompanionapp.domain

import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.FloraFauna
import no.hiof.friluftslivcompanionapp.models.enums.FloraFaunaSubclass
import no.hiof.friluftslivcompanionapp.models.enums.WeatherType

object FloraFaunaMapper {

    //TODO Test this.
    /**
     * @param species An object of type FloraFauna. Should be a subclass to FloraFauna.
     *
     * @return Returns an Enum resembling the subclass, complete with a stringResource label,
     * together with a boolean value to show whether subclass functionality is enabled in the
     * application.
     */
    fun mapClassToEnum(species: FloraFauna) : FloraFaunaSubclass? {

        val subclass : FloraFaunaSubclass? = when (species) {
            is Bird -> FloraFaunaSubclass.BIRD

            else -> null
        }

        return subclass
    }
}