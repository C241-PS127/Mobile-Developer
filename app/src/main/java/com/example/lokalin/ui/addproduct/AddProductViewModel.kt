package com.example.lokalin.ui.addproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.data.Repository
import com.example.data.response.ProductsItem
import com.example.utils.ResultState
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
    ): LiveData<ResultState<ProductsItem>> {
        return repository.addProduct(
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


    }
}