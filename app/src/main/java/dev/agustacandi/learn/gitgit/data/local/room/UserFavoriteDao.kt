package dev.agustacandi.learn.gitgit.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.agustacandi.learn.gitgit.model.UserFavorite

@Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM UserFavorite")
    fun getListUserFavorite(): LiveData<List<UserFavorite>>

    @Query("SELECT * FROM UserFavorite WHERE username = :username")
    fun getUserFavoriteByUsername(username: String): LiveData<UserFavorite>

    @Delete
    suspend fun deleteUserFavorite(userFavorite: UserFavorite)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserFavorite(userFavorite: UserFavorite)
}