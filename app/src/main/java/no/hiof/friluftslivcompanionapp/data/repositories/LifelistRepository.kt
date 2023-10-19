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

    /* This is for the lifelist List<floraFaunasighting>
    suspend fun saveLifeListToUser(lifelist: Lifelist) {
        val currentUser = auth.currentUser
            ?: throw IllegalStateException("No user currently signed in")

        val userId = currentUser.uid

        val userDocumentRef = firestore.collection("users").document(userId)
        val lifelistSubcollectionRef = userDocumentRef.collection("lifelist")

        // Convert the Lifelist to a Firestore-compatible format
        val lifelistData = lifelist.sightings.map { it.toMap() }

        // Batched writes allow you to combine multiple operations into a single atomic operation
        val batch = firestore.batch()

        lifelistData.forEach { sighting ->
            val documentRef = lifelistSubcollectionRef.document()  // Create a new document reference
            batch.set(documentRef, sighting)
        }

        // Await the batch commit using kotlinx.coroutines.tasks.await. If there's an exception, it will propagate.
        batch.commit().await()
    }

     */




    }


