package dev.agustacandi.learn.gitgit.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.agustacandi.learn.gitgit.model.UserFavorite

@Database(entities = [UserFavorite::class], version = 1, exportSchema = false)
abstract class UserFavoriteDatabase : RoomDatabase() {
    abstract fun userFavoriteDao(): UserFavoriteDao

    companion object {
        @Volatile
        private var instance: UserFavoriteDatabase? = null
        fun getInstance(context: Context): UserFavoriteDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserFavoriteDatabase::class.java,
                    "UserFavorite.db"
                ).build()
            }

    }
}