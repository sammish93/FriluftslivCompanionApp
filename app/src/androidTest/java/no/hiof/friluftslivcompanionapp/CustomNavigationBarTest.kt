package no.hiof.friluftslivcompanionapp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import no.hiof.friluftslivcompanionapp.models.enums.Screen
import no.hiof.friluftslivcompanionapp.ui.components.CustomNavigationBar
import org.junit.Before
import org.junit.Rule
import org.junit.Test
class CustomNavigationBarTest {
/*
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp(){
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            //navController.setGraph(R.navigation.nav_graph)
            CustomNavigationBar(navController = navController)
        }
    }


    @Test
    fun testNavigation() {

        composeTestRule.onNodeWithText("Trips").performClick()
        composeTestRule.onNodeWithText("Weather").performClick()
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Flora & Fauna").performClick()
        composeTestRule.onNodeWithText("Profile").performClick()

        assert(navController.currentDestination?.route == Screen.TRIPS.route)
        assert(navController.currentDestination?.route == Screen.WEATHER.route)
        assert(navController.currentDestination?.route == Screen.HOME.route)
        assert(navController.currentDestination?.route == Screen.FLORA_FAUNA.route)
        assert(navController.currentDestination?.route == Screen.PROFILE.route)
    }
*/
}