package dev.agustacandi.learn.gitgit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.agustacandi.learn.gitgit.data.local.datastore.ThemePreferences
import kotlinx.coroutines.launch

class ThemeViewModel(private val themePreferences: ThemePreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return themePreferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            themePreferences.saveThemeSetting(isDarkModeActive)
        }
    }
}