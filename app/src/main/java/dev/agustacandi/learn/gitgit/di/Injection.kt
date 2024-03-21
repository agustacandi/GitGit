package dev.agustacandi.learn.gitgit.di

import android.content.Context
import dev.agustacandi.learn.gitgit.repository.UserFavoriteRepository
import dev.agustacandi.learn.gitgit.data.local.room.UserFavoriteDatabase

object Injection {
    fun provideUserFavoriteRepository(context: Context): UserFavoriteRepository {
        val database = UserFavoriteDatabase.getInstance(context)
        val dao = database.userFavoriteDao()
        return UserFavoriteRepository.getInstance(dao)
    }
}