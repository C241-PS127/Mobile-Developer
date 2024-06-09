package com.example.lokalin.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.Product
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()

    init {
        allProducts()
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun allProducts() {
        _isLoading.postValue(true) // Set loading to true
        viewModelScope.launch {
            try {
                val stories = repository.getProducts()
                _products.postValue(stories)
            } catch (_: Exception) {

            }
        }
    }
}