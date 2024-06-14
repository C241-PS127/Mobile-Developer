package com.example.api

import com.example.response.AddCartResponse
import com.example.response.AddWishlistResponse
import com.example.response.Brand
import com.example.response.CartResponseItem
import com.example.response.CategoryResponseItem
import com.example.response.LoginResponse
import com.example.response.ProductResponse
import com.example.response.ProductsItem
import com.example.response.RegisterResponse
import com.example.response.UpdateCartResponse
import com.example.response.UserProfileResponseItem
import com.example.response.WishlistResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("fullName") fullName: String,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("picture") picture: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): LoginResponse

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): List<UserProfileResponseItem>

    @GET("products")
    suspend fun getAllProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ProductResponse

    @GET("products/brand/{brandName}")
    suspend fun getProductsByBrand(
        @Path("brandName") brandName: String
    ): List<ProductsItem>

    @GET("brands")
    suspend fun getBrands(): List<Brand>

    @GET("products/{id}")
    suspend fun getProductDetail(@Path("id") productId: String): Response<ProductsItem>

    @GET("categories")
    suspend fun getCategories(): List<CategoryResponseItem>

    @GET("wishlist/myWishlist")
    suspend fun getWishlist(@Header("Authorization") token: String): List<WishlistResponseItem>

    @FormUrlEncoded
    @POST("wishlist")
    suspend fun addWishlist(
        @Header("Authorization") token: String,
        @Field("productId") productId: String
    ): AddWishlistResponse

    @DELETE("wishlist/{wishlistId}")
    suspend fun deleteWishlist(
        @Header("Authorization") token: String,
        @Path("wishlistId") wishlistId: String
    ): List<WishlistResponseItem>

    @GET("cart/myCart")
    suspend fun getMyCart(@Header("Authorization") token: String): List<CartResponseItem>

    @FormUrlEncoded
    @POST("cart")
    suspend fun addCart(
        @Header("Authorization") token: String,
        @Field("productId") productId: String, @Field("count") count: Int
    ): AddCartResponse

    @FormUrlEncoded
    @PATCH("cart/{cartId}")
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Path("cartId") cartId: String, @Field("count") count: Int
    ): UpdateCartResponse

    @PATCH("cart/{cartId}")
    suspend fun hideCart(
        @Header("Authorization") token: String,
        @Path("cartId") cartId: String, @Field("isActive") isActive: Boolean
    ): UpdateCartResponse


    @DELETE("cart/{cartId}")
    suspend fun deleteCart(
        @Header("Authorization") token: String,
        @Path("cartId") cartId: String
    ): AddCartResponse

    @Multipart
    @POST("products")
    suspend fun addProducts(
        @Part("productName") productName: RequestBody,
        @Part("productDescription") productDescription: RequestBody,
        @Part("brandId") brandId: RequestBody,
        @Part("categoryId") categoryId: RequestBody,
        @Part("unitPrice") unitPrice: RequestBody,
        @Part("unitSize") unitSize: RequestBody,
        @Part("unitInStock") unitInStock: RequestBody,
        @Part("isAvailable") isAvailable: RequestBody,
        @Part("rating") rating: RequestBody,
        @Part file: MultipartBody.Part
    ): ProductsItem
}