package no.hiof.friluftslivcompanionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import no.hiof.friluftslivcompanionapp.ui.theme.FriluftslivCompanionAppTheme
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FriluftslivCompanionAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        // User is signed in, show the main content of your app
                        // Replace MainContent with your actual main content Composable
                        Greeting(name = "ashti")
                    } else {
                        // User is not signed in, show the sign-in activity
                        SignInActivity()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FriluftslivCompanionAppTheme {
        Greeting("Android")
    }
}