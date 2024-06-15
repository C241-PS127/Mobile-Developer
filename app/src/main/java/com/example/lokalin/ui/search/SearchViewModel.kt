package com.example.lokalin.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.Brand
import com.example.response.ProductItem
import com.example.response.ProductRecommendation
import com.example.response.ProductsItem
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
        _isLoading.postValue(true) // Set loading to true before initializing Pager
        viewModelScope.launch {
            try {
                val result = repository.getProductRecommendation(query)
                _productRecommendation.postValue(result)
            } catch (e: Exception) {
                // Handle the exception, e.g., show an error message
                Log.e("ViewModel", "Failed to fetch product recommendation", e)
            } finally {
                _isLoading.postValue(false) // Set loading to false after the operation is done
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