package com.example.lokalin.ui.checkout

import OrdersItem
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.example.repo.Repository
import com.example.response.CartResponseItem
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: Repository) : ViewModel() {

    fun addOrder(
        token: String, cartId: String, paymentId: String, freight: Int, shipperId: String
    ) {
        viewModelScope.launch {
            try {
                repository.addorder(token, cartId, freight, shipperId, paymentId)
            } catch (e: Exception) {
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


}