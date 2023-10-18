package no.hiof.friluftslivcompanionapp.data.repositories

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.mockito.kotlin.whenever
import kotlinx.coroutines.test.runTest
import no.hiof.friluftslivcompanionapp.models.User
import no.hiof.friluftslivcompanionapp.models.UserPreferences
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock



import kotlinx.coroutines.tasks.await
import org.mockito.kotlin.atLeastOnce

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private val mockFirestore: FirebaseFirestore = mock()
    private val mockAuth: FirebaseAuth = mock()
    private val mockCollection: CollectionReference = mock()
    private val mockDocument: DocumentReference = mock()
    private val mockFirebaseUser: FirebaseUser = mock()
    private val mockSetTask: Task<Void> = mock()

    @Before
    fun setUp() {
        userRepository = UserRepository(mockFirestore, mockAuth)

        whenever(mockFirestore.collection(any())).thenReturn(mockCollection)
        whenever(mockCollection.document(any())).thenReturn(mockDocument)
        whenever(mockAuth.currentUser).thenReturn(mockFirebaseUser)
        whenever(mockFirebaseUser.uid).thenReturn("testUserId")



    }

    @Test
    fun createUserWithValidUserAndAuthenticatedShouldReturnSuccess() = runTest {

        val user = User(
            userId = "testUserId",
            username = "testUser",
            email = "test@user.com",
            preferences = UserPreferences(),
            lifelist = null,
            tripActivity = null
        )


        val successfulTask = Tasks.forResult<Void>(null)
        `when`(mockDocument.set(any(), eq(SetOptions.merge()))).thenReturn(successfulTask)


        val result = userRepository.createUser(user)


        verify(mockDocument).set(
            any(),
            eq(SetOptions.merge())
        )

        assertTrue(result is OperationResult.Success)
    }

    @Test
    fun createUserWithoutAuthenticatedUserReturnsError() = runTest {
        whenever(mockAuth.currentUser).thenReturn(null)

        val user = User(
        userId = "testUserId",
        username = "testUser",
        email = "test@user.com",
        preferences = UserPreferences(),
        lifelist = null,
        tripActivity = null
        )
        val result = userRepository.createUser(user)

        verify(mockAuth, atLeastOnce()).currentUser

        assertTrue(result is OperationResult.Error)
    }

    @Test
    fun testFirestoreFailureWhenCreatingUserReturnError() = runTest {
        whenever(mockDocument.set(any(), any())).thenThrow(RuntimeException("Firestore exception"))

        val user = User(
            userId = "testUserId",
            username = "testUser",
            email = "test@user.com",
            preferences = UserPreferences(),
            lifelist = null,
            tripActivity = null
        )

        val result = userRepository.createUser(user)

        assertTrue(result is OperationResult.Error)
        assertEquals("Firestore exception", (result as OperationResult.Error).exception.message)
    }
}
