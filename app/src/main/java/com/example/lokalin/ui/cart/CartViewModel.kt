package com.example.lokalin.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import com.example.data.response.CartResponseItem
import com.example.data.response.UpdateCartResponse
import com.example.storyapp.data.pref.UserModel
import com.example.utils.ResultState
import kotlinx.coroutines.launch

class CartViewModel(private val repository: Repository) : ViewModel() {

    private var _cart = MutableLiveData<List<CartResponseItem>?>()
    val cart: LiveData<List<CartResponseItem>?> get() = _cart

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading

    private val _result = MutableLiveData<ResultState<UpdateCartResponse>?>()
    val result: LiveData<ResultState<UpdateCartResponse>?> get() = _result

    fun allCart(token: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val cart = repository.getMyCart(token)
                _cart.postValue(cart)
                _isLoading.postValue(false)
            } catch (_: Exception) {
                _isLoading.postValue(true)
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun updateCart(token: String, cartId: String, count: Int) {
        repository.updateCart(token, cartId, count).observeForever { resultState ->
            _result.postValue(resultState)
        }
    }

    fun deleteCart(token: String, cartId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCart(token, cartId)
                val currentList = _cart.value.orEmpty().toMutableList()
                currentList.removeAll { it.cartId == cartId }
                _cart.value = currentList
            } catch (_: Exception) {

            }
        }
    }
}