package dev.agustacandi.learn.gitgit.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.agustacandi.learn.gitgit.model.UserFavorite
import dev.agustacandi.learn.gitgit.repository.UserFavoriteRepository
import kotlinx.coroutines.launch

class UserFavoriteViewModel(private val userFavoriteRepository: UserFavoriteRepository) :
    ViewModel() {
    fun getListUserFavorite() = userFavoriteRepository.getListUserFavorite()

    fun getUserFavoriteByUsername(username: String) =
        userFavoriteRepository.getUserFavoriteByUsername(username)

    fun deleteUserFavorite(userFavorite: UserFavorite) {
        viewModelScope.launch {
            userFavoriteRepository.deleteUserFavorite(userFavorite)
        }
    }

    fun insertUserFavorite(userFavorite: UserFavorite) {
        viewModelScope.launch {
            userFavoriteRepository.insertUserFavorite(userFavorite)
        }
    }
}