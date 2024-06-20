package com.example.lokalin.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import com.example.data.response.Brand
import com.example.data.response.PredictionRequest
import com.example.data.response.PredictionResponse
import com.example.data.response.ProductItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _brands = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>> get() = _brands

    private val _productRecommendation = MutableLiveData<List<ProductItem>>()
    val productRecommendation: LiveData<List<ProductItem>>
        get() = _productRecommendation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _prediction = MutableLiveData<String>()
    val prediction: LiveData<String> get() = _prediction


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

    fun predict(url: String, seedText: String) {
        viewModelScope.launch {
            val request = PredictionRequest(seedText)
            repository.predict(url, request).enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(
                    call: Call<PredictionResponse>,
                    response: Response<PredictionResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        _prediction.value = response.body()!!.predicted_text
                    } else {
                        _prediction.value = "Failed to get response"
                    }
                }

                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    _prediction.value = "Error: ${t.message}"
                }
            })
        }
    }


    fun allBrands() {
        viewModelScope.launch {
            try {
                val stories = repository.getBrands()
                _brands.postValue(stories)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error getting recommendations", e)

            }
        }
    }
}