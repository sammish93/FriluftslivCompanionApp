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
import no.hiof.friluftslivcompanionapp.data.repositories.UserRepository
import no.hiof.friluftslivcompanionapp.models.UserPreferences

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var userRepository: UserRepository

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
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(no.hiof.friluftslivcompanionapp.R.style.Theme_FriluftslivCompanionApp)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                // Create or update the user document using the UserRepository
                val userData = no.hiof.friluftslivcompanionapp.models.User(
                    userId = user.uid,
                    email = user.email,
                    username = "default_username",
                    preferences = UserPreferences(),
                    lifelist = null,
                    tripActivity = null
                )

                userRepository.createUser(userData) { success ->
                    if (success) {
                        val mainIntent = Intent(this, MainActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    } else {
                        Log.e("SignInActivity", "Error creating/updating user document")
                    }
                }
            } else {
                Log.e("SignInActivity", "User is null")
            }
        } else {
            val errorMessage = "Sign-in failed. Please try again."
            Log.e("SignInActivity", errorMessage)
        }
    }

    /*
    //This code is blocked out for now the above code is making sure the userRepository is working
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            // User signed in successfully, you can handle this if needed
        } else {
            /*TODO add error message through pop-up or something*/
            val errorMessage = "Sign-in failed. Please try again."
            Log.e("SignInActivity", errorMessage)
        }
    }


     */
}
