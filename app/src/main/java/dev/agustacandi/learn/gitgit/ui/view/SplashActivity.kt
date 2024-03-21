package dev.agustacandi.learn.gitgit.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dev.agustacandi.learn.gitgit.data.local.datastore.ThemePreferences
import dev.agustacandi.learn.gitgit.data.local.datastore.dataStore
import dev.agustacandi.learn.gitgit.databinding.ActivitySplashBinding
import dev.agustacandi.learn.gitgit.ui.viewmodel.ThemeViewModel
import dev.agustacandi.learn.gitgit.ui.viewmodel.ThemeViewModelFactory
import dev.agustacandi.learn.gitgit.utils.SPLASHSCREEN_DURATION
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SplashActivity : AppCompatActivity() {

    private lateinit var activitySplashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        installSplashScreen()
        setContentView(activitySplashBinding.root)

        val pref = ThemePreferences.getInstance(application.dataStore)
        val themeViewModel =
            ViewModelProvider(this, ThemeViewModelFactory(pref))[ThemeViewModel::class.java]

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        lifecycleScope.launch {
            delay(SPLASHSCREEN_DURATION.seconds)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }

}