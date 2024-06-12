package com.example.lokalin.ui.wishlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.CategoryResponseItem
import com.example.response.UserProfileResponseItem
import com.example.response.WishlistResponseItem
import com.example.storyapp.data.pref.UserModel
import com.example.utils.ResultState
import kotlinx.coroutines.launch
import java.io.IOException

class WishlistViewModel(private val repository: Repository) : ViewModel() {

    private var _wishlist = MutableLiveData<List<WishlistResponseItem>?>()
    val wishlist: LiveData<List<WishlistResponseItem>?> get() = _wishlist

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading

    fun allWishlist(token: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val wishlist = repository.getWishlist(token)
                _wishlist.postValue(wishlist)
            } catch (_: Exception) {

            }
        }
    }

    fun deleteWishlist(token: String, wishlistId: String) {
        viewModelScope.launch {
            try {
                repository.deleteWishlist(token, wishlistId)
                val currentList = _wishlist.value.orEmpty().toMutableList()
                currentList.removeAll { it.wishlistId == wishlistId }
                _wishlist.value = currentList
            } catch (_: Exception) {

            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addWishlist(token: String, productId: String) {
        viewModelScope.launch {
            try {
                val response = repository.addWishlist(token, productId)
            } catch (e: Exception) {
            }
        }
    }

}