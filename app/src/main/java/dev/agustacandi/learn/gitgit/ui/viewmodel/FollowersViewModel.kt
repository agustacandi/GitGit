package dev.agustacandi.learn.gitgit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.agustacandi.learn.gitgit.data.response.ItemsItem
import dev.agustacandi.learn.gitgit.data.retrofit.ApiConfig
import dev.agustacandi.learn.gitgit.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private var _listFollowers = MutableLiveData<List<ItemsItem?>>()
    val listFollowers: LiveData<List<ItemsItem?>> = _listFollowers

    private var _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> = _loader

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getUserFollowersByUsername(username: String) {
        _loader.value = true
        val client = ApiConfig.getServiceApi().getUserFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _loader.value = false
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                } else {
                    _snackbarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _loader.value = false
                _snackbarText.value = Event(t.message.toString())
            }

        })
    }

}