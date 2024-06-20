package com.example.data.api

import OrderStatusItem
import OrdersItem
import OrdersResponse
import com.example.data.response.AddCartResponse
import com.example.data.response.AddWishlistResponse
import com.example.data.response.Brand
import com.example.data.response.CartResponseItem
import com.example.data.response.CategoryResponseItem
import com.example.data.response.LoginResponse
import com.example.data.response.Payment
import com.example.data.response.PredictionRequest
import com.example.data.response.PredictionResponse
import com.example.data.response.ProductRecommendation
import com.example.data.response.ProductResponse
import com.example.data.response.ProductsItem
import com.example.data.response.RegisterResponse
import com.example.data.response.ShippersItem
import com.example.data.response.UpdateCartResponse
import com.example.data.response.UserProfileResponseItem
import com.example.data.response.WishlistResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
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
import retrofit2.http.Url


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
        @Query("page") page: Int, @Query("limit") limit: Int
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
        @Header("Authorization") token: String, @Field("productId") productId: String
    ): AddWishlistResponse

    @DELETE("wishlist/{wishlistId}")
    suspend fun deleteWishlist(
        @Header("Authorization") token: String, @Path("wishlistId") wishlistId: String
    ): List<WishlistResponseItem>

    @GET("cart/myCart")
    suspend fun getMyCart(@Header("Authorization") token: String): List<CartResponseItem>

    @FormUrlEncoded
    @POST("cart")
    suspend fun addCart(
        @Header("Authorization") token: String,
        @Field("productId") productId: String,
        @Field("count") count: Int
    ): AddCartResponse

    @FormUrlEncoded
    @PATCH("cart/{cartId}")
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Path("cartId") cartId: String,
        @Field("count") count: Int
    ): UpdateCartResponse

    @PATCH("cart/{cartId}")
    suspend fun hideCart(
        @Header("Authorization") token: String,
        @Path("cartId") cartId: String,
        @Field("isActive") isActive: Boolean
    ): UpdateCartResponse


    @DELETE("cart/{cartId}")
    suspend fun deleteCart(
        @Header("Authorization") token: String, @Path("cartId") cartId: String
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

    @GET("recommendation")
    suspend fun getRecommendations(@Query("query") query: String): ProductRecommendation

    @DELETE("products/{productId}")
    suspend fun deleteProduct(@Path("productId") productId: String)

    @FormUrlEncoded
    @POST("orders")
    suspend fun addOrder(
        @Header("Authorization") token: String,
        @Field("cartId") cartId: String,
        @Field("paymentId") paymentId: String,
        @Field("freight") freight: Int,
        @Field("shipperId") shipperId: String
    ): OrdersResponse

    @GET("payments")
    suspend fun getPayments(): List<Payment>
    @GET("shippers")
    suspend fun getShippers(): List<ShippersItem>

    @GET("orders/process")
    suspend fun getOrdersProcessing(
        @Header("Authorization") token: String
    ): List<OrdersItem>

    @GET("orders/finished")
    suspend fun getFinished(
        @Header("Authorization") token: String
    ): List<OrdersItem>

    @GET("orders")
    suspend fun getShopOrders(
    ): List<OrdersItem>

    @GET("orderStatus")
    suspend fun getOrderStatus(
    ): List<OrderStatusItem>

    @POST
    fun predict(@Url url: String, @Body request: PredictionRequest): Call<PredictionResponse>

}