package com.example.lokalin.ui.myproducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import com.example.data.response.ProductsItem
import kotlinx.coroutines.launch

class MyProductViewModel(private val repository: Repository) : ViewModel() {
    private var _products = MutableLiveData<List<ProductsItem>?>()
    val products: LiveData<List<ProductsItem>?> get() = _products


    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading


    fun getProductsByBrand(brandName: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val product = repository.getProductsByBrand(brandName)
                _products.postValue(product)
                _isLoading.postValue(false)

            } catch (_: Exception) {
                _isLoading.postValue(true)

            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(productId)
            } catch (_: Exception) {

            }
        }
    }

}