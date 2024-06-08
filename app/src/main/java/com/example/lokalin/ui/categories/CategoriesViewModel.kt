package com.example.lokalin.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.CategoryResponseItem
import com.example.response.Product
import com.example.utils.ResultState
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: Repository) : ViewModel() {
//    fun getCategories(): LiveData<ResultState<List<CategoryResponseItem>>> {
//        return repository.getCategories()
//    }

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