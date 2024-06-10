package com.example.repo

import androidx.datastore.core.IOException
import androidx.lifecycle.liveData
import com.example.api.ApiService
import com.example.response.Brand
import com.example.response.CategoryResponseItem
import com.example.response.LoginResponse
import com.example.response.Product
import com.example.response.RegisterResponse
import com.example.response.WishlistResponseItem
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.example.utils.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class Repository private constructor(
    private var apiService: ApiService, private val userPreference: UserPreference
) {
    suspend fun getProducts(): List<Product> {
        return apiService.getAllProducts()
    }

    fun updateApiService(apiService: ApiService) {
        this.apiService = apiService
    }

    fun registerData(
        name: String, address: String, phoneNumber: String, email: String, password: String
    ) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(
                name, address, phoneNumber, email, picture = "", password = password
            )
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
//            emit(errorResponse.message?.let { ResultState.Error(it) })
        }
    }

    fun getProfile(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getProfile(token)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorMessage = when (e.code()) {
                401 -> "Unauthorized access. Please check your credentials."
                403 -> "Forbidden access. You don't have permission to access this resource."
                404 -> "Profile not found."
                500 -> "Internal server error. Please try again later."
                else -> "Something went wrong. Error code: ${e.code()}"
            }
            emit(ResultState.Error(errorMessage))
        }
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun getBrands(): List<Brand> {
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

    suspend fun getCategories(): List<CategoryResponseItem> {
        return apiService.getCategories()
    }

    suspend fun getWishlist(): List<WishlistResponseItem> {
        return apiService.getWishlist()
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
