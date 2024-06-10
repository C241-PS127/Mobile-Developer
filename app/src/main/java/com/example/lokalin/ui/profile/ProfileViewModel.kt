package com.example.lokalin.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repo.Repository
import com.example.response.UserProfileResponseItem
import com.example.storyapp.data.pref.UserModel
import com.example.utils.ResultState

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    fun getProfile(token: String): LiveData<ResultState<List<UserProfileResponseItem>>> {
        return repository.getProfile(token)
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}