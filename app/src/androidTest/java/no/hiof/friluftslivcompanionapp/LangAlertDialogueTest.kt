package no.hiof.friluftslivcompanionapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import no.hiof.friluftslivcompanionapp.ui.screens.SettingsScreen
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import no.hiof.friluftslivcompanionapp.models.enums.SupportedLanguage
import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LangAlertDialogueTest {
/*
    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testLanguageChange() {
            // Start test
        composeTestRule.setContent {
            var selectedLanguage by remember { mutableStateOf(SupportedLanguage.ENGLISH) }

            SettingsScreen(
                    navController = rememberNavController(),
            )
        }

            // Åpner SettingsScreen og verifiserer at Language-delen er tilstede
        composeTestRule.onNodeWithText("Language - English").assertExists()

            // Trykker på Language-delen for å åpne LangAlertDialogue
        composeTestRule.onNodeWithText("Language - English").performClick()

            // Verifiser at LangAlertDialogue er åpnet
        composeTestRule.onNodeWithText("Language").assertExists()
        composeTestRule.onNodeWithText("English").assertExists()
        composeTestRule.onNodeWithText("Norwegian").assertExists()

            // Endre språket til norsk
        composeTestRule.onNodeWithText("Norwegian").performClick()

            // Simulerer trykk på Confirm-knappen i LangAlertDialogue
        composeTestRule.onNodeWithText("Confirm").performClick()

            // Verifiser at språket har endret seg til norsk
        composeTestRule.onNodeWithText("Språk - Norsk").assertExists()
    }
    */
}



