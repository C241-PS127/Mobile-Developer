package com.example.lokalin.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.Brand
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: Repository) : ViewModel() {

    private val _brands = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>> get() = _brands

    init {
        allBrands()
    }

    fun allBrands() {
        viewModelScope.launch {
            try {
                val stories = repository.getBrands()
                _brands.postValue(stories)
            } catch (_: Exception) {
            }
        }
    }
}