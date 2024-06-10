package com.example.lokalin.ui.wishlist

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

class WishlistViewModel(private val repository: Repository) : ViewModel() {

    private val _wishlist = MutableLiveData<List<WishlistResponseItem>>()
    val wishlist: LiveData<List<WishlistResponseItem>> get() = _wishlist

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading

    init {
        allWishlist()
    }

    fun allWishlist() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val wishlist = repository.getWishlist()
                _wishlist.postValue(wishlist)
            } catch (_: Exception) {

            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}