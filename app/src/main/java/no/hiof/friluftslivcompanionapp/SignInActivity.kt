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
                // Create or update the user document using the UserRepository
                val userData = no.hiof.friluftslivcompanionapp.models.User(
                    userId = user.uid,
                    email = user.email,
                    username = "default_username",
                    preferences = UserPreferences(),
                    lifelist = null,
                    tripActivity = null
                )
                launch {
                    userRepository.createUser(userData)

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
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
