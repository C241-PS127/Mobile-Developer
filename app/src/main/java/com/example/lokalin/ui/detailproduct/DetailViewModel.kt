package com.example.lokalin.ui.detailproduct

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.IOException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.Product
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private val _productDetail = MutableLiveData<Product>()
    val productDetail: LiveData<Product> get() = _productDetail

    fun fetchProductDetail(productId: String) {
        viewModelScope.launch {
            try {
                val product = repository.getProductDetail(productId)
                _productDetail.value = product
            } catch (e: IOException) {
                // Handle error
            }
        }
    }
    fun addCart(token: String, productId: String, count: Int) {
        viewModelScope.launch {
            try {
                val product = repository.addCart(token, productId, count)
            } catch (e: IOException) {
                // Handle error
            }
        }
    }
}



