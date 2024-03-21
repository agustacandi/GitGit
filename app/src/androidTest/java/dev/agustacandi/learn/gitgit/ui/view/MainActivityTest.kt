package dev.agustacandi.learn.gitgit.ui.view

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import dev.agustacandi.learn.gitgit.R
import dev.agustacandi.learn.gitgit.utils.TWO_MILLISECOND
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testSwitchThemeButton() {

        onView(withId(R.id.theme_menu)).check(matches(isDisplayed()))
        onView(withId(R.id.theme_menu)).perform(click())
        Thread.sleep(TWO_MILLISECOND)
    }
}