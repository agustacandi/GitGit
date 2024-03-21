package dev.agustacandi.learn.gitgit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.agustacandi.learn.gitgit.data.remote.response.ItemsItem
import dev.agustacandi.learn.gitgit.data.remote.response.UserResponse
import dev.agustacandi.learn.gitgit.data.remote.retrofit.ApiConfig
import dev.agustacandi.learn.gitgit.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _users = MutableLiveData<List<ItemsItem>?>()
    val users: LiveData<List<ItemsItem>?> = _users

    private val _usernameNotFoundText = MutableLiveData<String>()
    val usernameNotFoundText: LiveData<String> = _usernameNotFoundText

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> = _loader

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    init {
        searchUserByUsername()
    }

    fun searchUserByUsername(username: String = "a") {
        _usernameNotFoundText.value = ""
        _loader.value = true
        val client = ApiConfig.getServiceApi().getSearchUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _loader.value = false
                if (response.isSuccessful) {
                    _users.value = response.body()?.items
                    _usernameNotFoundText.value =
                        if (users.value!!.isEmpty()) "Username not found" else ""
                } else {
                    _usernameNotFoundText.value = "Error fetch data"
                    _snackbarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _usernameNotFoundText.value = "Error fetch data"
                _loader.value = false
                _snackbarText.value = Event(t.message.toString())
            }

        })
    }
}