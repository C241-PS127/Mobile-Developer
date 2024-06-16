package com.example.lokalin.ui.checkout

import OrdersItem
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.CartResponseItem
import com.example.response.Payment
import com.example.response.ShippersItem
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(private val repository: Repository) : ViewModel() {

    private var _payment = MutableLiveData<List<Payment>>()
    val payment: LiveData<List<Payment>> get() = _payment

    private var _shippers = MutableLiveData<List<ShippersItem>>()
    val shippers: LiveData<List<ShippersItem>> get() = _shippers

    private var _cart = MutableLiveData<List<CartResponseItem>?>()
    val cart: LiveData<List<CartResponseItem>?> get() = _cart


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

    fun getPayments() {
        viewModelScope.launch {
            try {
                val payments = repository.getPayment()
                _payment.value = payments
                println("Payments: $payments")  // Logging

            } catch (e: Exception) {
                println("Error fetching payments: ${e.message}")  // Logging
            }
        }
    }

    fun allCart(token: String) {
        viewModelScope.launch {
            try {
                val cart = repository.getMyCart(token)
                _cart.postValue(cart)
            } catch (_: Exception) {
            }
        }
    }

    fun getShippers() {
        viewModelScope.launch {
            try {
                val shippers = repository.getShippers()
                _shippers.value = shippers
                println("Shippers: $shippers")  // Logging

            } catch (e: Exception) {
                println("Error fetching payments: ${e.message}")  // Logging
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


}