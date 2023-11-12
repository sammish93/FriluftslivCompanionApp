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
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.hiof.friluftslivcompanionapp.data.repositories.UserRepository
import no.hiof.friluftslivcompanionapp.models.User
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
        if (intent.getBooleanExtra("linkAccount", false)) {

            setupAuthUIForUpgrade()
        } else {

            showSignInOptions()
        }
    }

    private fun setupAuthUIForUpgrade() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()

        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.Theme_FriluftslivCompanionApp)
            .enableAnonymousUsersAutoUpgrade()
            .build()

        signInLauncher.launch(signInIntent)
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
            .setTheme(R.style.Theme_FriluftslivCompanionApp)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = auth.currentUser
            if (user != null) {
                if (!user.isAnonymous) {

                    handleAccountUpgrade(user)

                } else {
                    launch {
                        userRepository.createUser(user.toUserModel())
                        navigateToMainActivity()
                    }
                }
            } else {

                Log.e("SignInActivity", "User is not authenticated")

            }
        } else {

            result.idpResponse?.error?.let { error ->
                Log.e("SignInActivity", "Error during sign-in or account upgrade", error)
            }
        }
    }

    fun FirebaseUser.toUserModel(): User{
        return User(
            userId = this.uid,
            email = this.email,
            username = this.displayName ?: "",
            preferences = UserPreferences()

        )
    }

    private fun handleAccountUpgrade(user: FirebaseUser) {
        launch {

            val existingUserData = userRepository.getUser(user.uid)
            existingUserData.email = user.email
            existingUserData.username = user.displayName ?: ""
            userRepository.updateUser(existingUserData)
            navigateToMainActivity()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
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
