package dev.agustacandi.learn.gitgit.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.agustacandi.learn.gitgit.di.Injection
import dev.agustacandi.learn.gitgit.repository.UserFavoriteRepository

class UserFavoriteViewModelFactory(private val userFavoriteRepository: UserFavoriteRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
            return UserFavoriteViewModel(userFavoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UserFavoriteViewModelFactory? = null
        fun getInstance(context: Context): UserFavoriteViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UserFavoriteViewModelFactory(
                    Injection.provideUserFavoriteRepository(
                        context
                    )
                )
            }.also { instance = it }
    }
}