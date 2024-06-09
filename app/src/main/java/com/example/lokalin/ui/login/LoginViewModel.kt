package com.example.lokalin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.LoginResponse
import com.example.storyapp.data.pref.UserModel
import com.example.utils.ResultState
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _users = MutableLiveData<ResultState<LoginResponse>?>()
    val users: LiveData<ResultState<LoginResponse>?> get() = _users
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String) {
        repository.login(email, password).observeForever() { resultState ->
            _users.postValue(resultState)
        }
    }

}