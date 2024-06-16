package com.example.lokalin.ui.Order.history

import OrdersItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: Repository) : ViewModel() {

    private val _ordersProcessing = MutableLiveData<List<OrdersItem>>()
    val ordersProcessing: LiveData<List<OrdersItem>> get() = _ordersProcessing

    fun fetchOrdersFinished(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getFinished(token)
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
