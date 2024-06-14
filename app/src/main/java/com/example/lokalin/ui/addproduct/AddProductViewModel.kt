package com.example.lokalin.ui.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repo.Repository
import com.example.response.UpdateCartResponse
import com.example.utils.ResultState
import kotlinx.coroutines.launch
import java.io.File

class AddProductViewModel(private val repository: Repository) : ViewModel() {
    fun addProduct(
        productName: String,
        productDescription: String,
        brandId: String,
        categoryId: String,
        unitPrice: Int,
        unitSize: String,
        unitInStock: Int,
        isAvailable: Boolean,
        rating: String,
        imageFile: File
    ) {
        viewModelScope.launch {
            try {
                val cart = repository.addProduct(
                    productName,
                    productDescription,
                    brandId,
                    categoryId,
                    unitPrice,
                    unitSize,
                    unitInStock,
                    isAvailable,
                    rating,
                    imageFile
                )

            } catch (_: Exception) {
            }
        }
    }
}