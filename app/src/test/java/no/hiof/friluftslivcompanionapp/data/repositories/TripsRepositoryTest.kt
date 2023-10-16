package no.hiof.friluftslivcompanionapp.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TripsRepositoryTest {
    @Mock
    private lateinit var firestore: FirebaseFirestore

    @Mock
    private lateinit var auth: FirebaseAuth

    private lateinit var tripsRepository: TripsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        tripsRepository = TripsRepository(firestore, auth)
    }


    @Test
    fun saveAHikeTOFireStoreTest() = runTest {

    }

}