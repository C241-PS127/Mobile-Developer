package com.example.lokalin.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Repository
import com.example.data.response.CategoryResponseItem
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: Repository) : ViewModel() {

    private val _categories = MutableLiveData<List<CategoryResponseItem>>()
    val categories: LiveData<List<CategoryResponseItem>> get() = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _isLoading

    init {
        allCategories()
    }

    fun allCategories() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val stories = repository.getCategories()
                _categories.postValue(stories)
            } catch (_: Exception) {

            }
        }
    }
}