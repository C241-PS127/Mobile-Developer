package com.example.lokalin.ui.shoporders

import OrderStatusItem
import OrdersItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class ShopOrdersViewModel(private val repository: Repository) : ViewModel() {

    private val _shoporders = MutableLiveData<List<OrdersItem>>()
    val shoporders: LiveData<List<OrdersItem>> get() = _shoporders


    private val _status = MutableLiveData<List<OrderStatusItem>>()
    val status: LiveData<List<OrderStatusItem>> get() = _status

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading

    init {
        getShopOrders()
        getOrderStatus()
    }

    fun getShopOrders() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = repository.getShopOrders()
                _shoporders.value = response
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _shoporders.value = emptyList()
                _isLoading.postValue(true)
            }
        }
    }

    fun getOrderStatus() {
        viewModelScope.launch {
            try {
                val response = repository.getOrderStatus()
                _status.value = response
            } catch (e: Exception) {
                _status.value = emptyList()
            }
        }
    }
}
