package dev.agustacandi.learn.gitgit.repository

import androidx.lifecycle.LiveData
import dev.agustacandi.learn.gitgit.model.UserFavorite
import dev.agustacandi.learn.gitgit.data.local.room.UserFavoriteDao

class UserFavoriteRepository private constructor(
    private val userFavoriteDao: UserFavoriteDao,
) {

    fun getListUserFavorite(): LiveData<List<UserFavorite>> {
        return userFavoriteDao.getListUserFavorite()
    }

    fun getUserFavoriteByUsername(username: String): LiveData<UserFavorite> {
        return userFavoriteDao.getUserFavoriteByUsername(username)
    }

    suspend fun deleteUserFavorite(userFavorite: UserFavorite) {
        return userFavoriteDao.deleteUserFavorite(userFavorite)
    }

    suspend fun insertUserFavorite(userFavorite: UserFavorite) {
        return userFavoriteDao.insertUserFavorite(userFavorite)
    }

    companion object {
        @Volatile
        private var instance: UserFavoriteRepository? = null
        fun getInstance(
            userFavoriteDao: UserFavoriteDao,
        ): UserFavoriteRepository = instance ?: synchronized(this) {
            instance ?: UserFavoriteRepository(userFavoriteDao).also {
                instance = it
            }
        }
    }
}