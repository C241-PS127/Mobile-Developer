package com.example.lokalin.ui.Order.order

import OrdersItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.UserProfileResponseItem
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: Repository) : ViewModel() {

    private val _ordersProcessing = MutableLiveData<List<OrdersItem>>()
    val ordersProcessing: LiveData<List<OrdersItem>> get() = _ordersProcessing

    fun fetchOrdersProcessing(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getOrdersProcessing(token)
                _ordersProcessing.value = response
            } catch (e: Exception) {
                _ordersProcessing.value = emptyList()
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}
