package no.hiof.friluftslivcompanionapp.data.repositories
import no.hiof.friluftslivcompanionapp.data.network.Result
import no.hiof.friluftslivcompanionapp.domain.BirdObservations
import no.hiof.friluftslivcompanionapp.models.Bird
import javax.inject.Inject

class FloraFaunaRepository@Inject constructor(
    private val birdObservations: BirdObservations
) {
    suspend fun getBirdsByLocation(location: String): Result<List<Bird>> {
        return birdObservations.getRecentObservations(regionCode = location)
    }
}