package dev.agustacandi.learn.gitgit.data.retrofit

import dev.agustacandi.learn.gitgit.data.response.DetailUserResponse
import dev.agustacandi.learn.gitgit.data.response.ItemsItem
import dev.agustacandi.learn.gitgit.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchUser(
        @Query("q") q: String
    ): Call<UserResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}