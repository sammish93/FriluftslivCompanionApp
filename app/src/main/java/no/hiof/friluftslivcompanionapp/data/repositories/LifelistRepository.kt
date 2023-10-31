package no.hiof.friluftslivcompanionapp.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.FloraFaunaSighting
import no.hiof.friluftslivcompanionapp.models.Lifelist
import java.util.Calendar
import javax.inject.Inject

class LifelistRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    // Responsible for retrieving a single user's lifelist. Used by the UserRepository.






    suspend fun addSightingToLifeList(newSighting: FloraFaunaSighting) {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No user currently signed in")

        val userId = currentUser.uid
        val userDocumentRef = firestore.collection("users").document(userId)
        val lifelistSubcollectionRef = userDocumentRef.collection("lifelist")


        val sightingData = newSighting.toMap()


        lifelistSubcollectionRef.add(sightingData).await()
    }


    suspend fun getAllItemsInLifeList(): List<Lifelist> {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No user currently signed in")

        val userId = currentUser.uid
        val userDocumentRef = firestore.collection("users").document(userId)
        val lifelistSubcollectionRef = userDocumentRef.collection("lifelist")

        val snapshot = lifelistSubcollectionRef.get().await()

        return snapshot.documents.mapNotNull { document ->
            FloraFaunaSighting.fromMap(document.data as Map<String, Any>)?.let { sighting ->
                Lifelist(sightings = sighting)
            }
        }
    }

    suspend fun countUniqueSpeciesSightedThisYear(): Int {
        val allLifelists = getAllItemsInLifeList()


        val allSightings = allLifelists.map { it.sightings }

        val startOfYear = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        val filteredSightingsForThisYear = allSightings.filter { sighting -> sighting.date.after(startOfYear) }

        return filteredSightingsForThisYear.map { it.species }.distinct().count()
    }













}


