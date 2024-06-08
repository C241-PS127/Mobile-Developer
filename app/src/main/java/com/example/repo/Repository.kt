package com.example.repo

import androidx.datastore.core.IOException
import com.example.api.ApiService
import com.example.response.Brand
import com.example.response.Product
import com.example.storyapp.data.pref.UserPreference

class Repository private constructor(
    private var apiService: ApiService, private val userPreference: UserPreference
){
    suspend fun getProducts(): List<Product> {
        return apiService.getAllProducts()
    }

    suspend fun getBrands(): List<Brand>{
        return apiService.getBrands()
    }

    suspend fun getProductDetail(productId: String): Product {
        val response = apiService.getProductDetail(productId)
        if (response.isSuccessful) {
            val products = response.body() ?: throw IOException("Product not found")
            return products.firstOrNull() ?: throw IOException("Product not found")
        } else {
            throw IOException("Error fetching product detail")
        }
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference) =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}
