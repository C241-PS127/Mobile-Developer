package com.example.lokalin.ui.detailproduct

import androidx.datastore.core.IOException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import com.example.data.response.AddCartResponse
import com.example.data.response.ProductsItem
import com.example.utils.ResultState
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : ViewModel() {
    private val _productDetail = MutableLiveData<ProductsItem>()
    val productDetail: LiveData<ProductsItem> get() = _productDetail

    private val _cart = MutableLiveData<ResultState<AddCartResponse>>()
    val cart: LiveData<ResultState<AddCartResponse>?> get() = _cart

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading

    fun fetchProductDetail(productId: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val product = repository.getProductDetail(productId)
                _productDetail.value = product
                _isLoading.postValue(false)

            } catch (e: IOException) {
                _isLoading.postValue(true)
            }
        }
    }

    fun addCart(token: String, productId: String, count: Int) {
        viewModelScope.launch {
            try {
                val response = repository.addCart(token, productId, count)
            } catch (e: Exception) {
            }
        }
    }
}



