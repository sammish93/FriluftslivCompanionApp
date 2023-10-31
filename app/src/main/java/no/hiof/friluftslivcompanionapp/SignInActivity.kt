package no.hiof.friluftslivcompanionapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.repositories.ActivityRepository
import no.hiof.friluftslivcompanionapp.data.repositories.LifelistRepository
import no.hiof.friluftslivcompanionapp.data.repositories.UserRepository
import no.hiof.friluftslivcompanionapp.models.Bird
import no.hiof.friluftslivcompanionapp.models.FloraFaunaSighting
import no.hiof.friluftslivcompanionapp.models.Lifelist
import no.hiof.friluftslivcompanionapp.models.Location
import no.hiof.friluftslivcompanionapp.models.UserPreferences
import java.time.LocalDateTime
import java.util.Date

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var lifeListRepository: LifelistRepository

    @Inject
    lateinit var activityRepository: ActivityRepository

    private val signInLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            onSignInResult(res)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSignInOptions()
    }

    private fun showSignInOptions() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.AnonymousBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(no.hiof.friluftslivcompanionapp.R.style.Theme_FriluftslivCompanionApp)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = auth.currentUser

            if (user != null) {
                launch {
                    // You can define these values as needed or get them dynamically
                    val location = Location(58.99107, 6.137688)
                  //  val bird = Bird(null, "Big bird", 2, "Big yellow bird", null, LocalDateTime.now(),location)
                   // val bird2 = Bird(null, "Big bird2", 2, "Big yellow bird", null, LocalDateTime.now(),location)
                    /*
                    val sampleSighting = FloraFaunaSighting(
                        species = bird,
                        date = Date(),
                        location = location
                    )
                    val anotherSample = FloraFaunaSighting(
                        species = bird2,
                        date = Date(),
                        location = location
                    )
                    
                     */
                    //val lifelist = Lifelist()
                   // val waht = lifeListRepository.addSightingToLifeList(anotherSample)

                    val sampleDate = Date()
                    val sampleTripId = "qtSfnEnJutxgJqCc94l0"

                    val userData = no.hiof.friluftslivcompanionapp.models.User(
                        userId = user.uid,
                        email = user.email,
                        username = "this user name",
                        preferences = UserPreferences(),


                    )

                    userRepository.createUser(userData)

                    activityRepository.addTripActivityToUser(sampleTripId,sampleDate)

                    navigateToMainActivity()
                }

            } else {
                Log.e("SignInActivity", "User is null")
            }
        } else {
            val errorMessage = "Sign-in failed. Please try again."
            Log.e("SignInActivity", errorMessage)
        }
    }
    private fun launch(block: suspend () -> Unit) {
        kotlinx.coroutines.GlobalScope.launch {
            withContext(Dispatchers.IO) {
                block()
            }
        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
