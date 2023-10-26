package no.hiof.friluftslivcompanionapp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
@RunWith(AndroidJUnit4::class)
class LangAlertDialogueTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testLanguageChange() {
        // Find the "Confirm" button and click on it
        Espresso.onView(ViewMatchers.withText("Confirm")).perform(ViewActions.click())

        // Find the element displaying the selected language
        val languageElement = Espresso.onView(ViewMatchers.withId(R.string.lang_english)) // Replace with the actual ID of the element

        // Assert that the content of the element matches the expected language
        val expectedLanguage = "English" // Replace with the expected language
        languageElement.check(ViewAssertions.matches(ViewMatchers.withText(expectedLanguage)))
    }
}
*/
