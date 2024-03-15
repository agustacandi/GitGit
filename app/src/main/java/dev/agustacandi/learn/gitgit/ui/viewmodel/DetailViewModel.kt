package dev.agustacandi.learn.gitgit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.agustacandi.learn.gitgit.data.response.DetailUserResponse
import dev.agustacandi.learn.gitgit.data.retrofit.ApiConfig
import dev.agustacandi.learn.gitgit.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private var _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private var _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> = _loader

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getUserDetail(username: String = "torvalds") {
        _loader.value = true
        val client = ApiConfig.getServiceApi().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _loader.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                }else {
                    _snackbarText.value = Event(response.message())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _loader.value = false
                _snackbarText.value = Event(t.message.toString())
            }

        })
    }

}