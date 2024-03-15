package dev.agustacandi.learn.gitgit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.agustacandi.learn.gitgit.data.response.ItemsItem
import dev.agustacandi.learn.gitgit.data.response.UserResponse
import dev.agustacandi.learn.gitgit.data.retrofit.ApiConfig
import dev.agustacandi.learn.gitgit.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _users = MutableLiveData<List<ItemsItem>?>()
    val users: LiveData<List<ItemsItem>?> = _users

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> = _loader

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        searchUserByUsername()
    }

    fun searchUserByUsername(username: String = "a") {
        _loader.value = true
        val client = ApiConfig.getServiceApi().getSearchUser(username)
        client.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _loader.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()?.items
                } else {
                    _snackbarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _loader.value = false
                _snackbarText.value = Event(t.message.toString())
            }

        })
    }
}