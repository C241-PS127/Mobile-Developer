package com.example.lokalin.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import com.example.data.response.Brand
import com.example.data.response.ProductItem
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _brands = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>> get() = _brands

    private val _productRecommendation = MutableLiveData<List<ProductItem>>()
    val productRecommendation: LiveData<List<ProductItem>>
        get() = _productRecommendation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        allBrands()
    }

    fun getProductRecommendation(query: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val result = repository.getProductRecommendation(query)
                _productRecommendation.postValue(result)
                _isLoading.postValue(true)
            } catch (e: Exception) {
                Log.e("ViewModel", "Failed to fetch product recommendation", e)
                _isLoading.postValue(false)
            }
        }
    }


    fun allBrands() {
        viewModelScope.launch {
            try {
                val stories = repository.getBrands()
                _brands.postValue(stories)
            } catch (e : Exception) {
                Log.e("SearchViewModel", "Error getting recommendations", e)

            }
        }
    }
}