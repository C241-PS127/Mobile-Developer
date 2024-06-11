package com.example.lokalin.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.CartResponseItem
import com.example.response.WishlistResponseItem
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class CartViewModel(private val repository: Repository) : ViewModel() {

    private var _cart = MutableLiveData<List<CartResponseItem>?>()
    val cart: LiveData<List<CartResponseItem>?> get() = _cart

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading

    fun allCart(token: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val cart = repository.getMyCart(token)
                _cart.postValue(cart)
            } catch (_: Exception) {

            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}