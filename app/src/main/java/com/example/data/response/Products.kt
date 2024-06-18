package com.example.data.response

import com.google.gson.annotations.SerializedName

data class ProductsItem(
    @SerializedName("ProductId") val productId: String,
    @SerializedName("ProductName") val productName: String,
    @SerializedName("ProductDescription") val productDescription: String,
    @SerializedName("BrandId") val brandId: String,
    @SerializedName("CategoryId") val categoryId: String,
    @SerializedName("UnitPrice") val unitPrice: Double,
    @SerializedName("UnitSize") val unitSize: String,
    @SerializedName("UnitInStock") val unitInStock: Int,
    @SerializedName("isAvailable") val isAvailable: Boolean,
    @SerializedName("Picture") val picture: String,
    @SerializedName("BrandName") val brandName: String,
    @SerializedName("CategoryName") val categoryName: String,
    @SerializedName("ImgUrl") val imgUrl: String? = null
)

data class Pagination(
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("currentPage") val currentPage: Int,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("pageSize") val pageSize: Int
)

data class ProductResponse(
    @SerializedName("products") val products: List<ProductsItem>,
    @SerializedName("pagination") val pagination: Pagination
)

