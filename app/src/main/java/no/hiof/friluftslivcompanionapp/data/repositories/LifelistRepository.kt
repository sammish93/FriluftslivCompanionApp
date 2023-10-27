package no.hiof.friluftslivcompanionapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import no.hiof.friluftslivcompanionapp.models.FloraFaunaSighting
import no.hiof.friluftslivcompanionapp.models.Lifelist
import javax.inject.Inject

class LifelistRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
){
    // Responsible for retrieving a single user's lifelist. Used by the UserRepository.


    suspend fun saveLifeListToUser(lifelist: Lifelist) {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No user currently signed in")

        val userId = currentUser.uid

        val userDocumentRef = firestore.collection("users").document(userId)
        val lifelistSubcollectionRef = userDocumentRef.collection("lifelist")


        val lifelistData = lifelist.sightings.map { it.toMap() }


        val batch = firestore.batch()

        lifelistData.forEach { sighting ->
            val documentRef = lifelistSubcollectionRef.document()
            batch.set(documentRef, sighting)
        }


        batch.commit().await()
    }






    }


