package com.example.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val products: List<Product>
)

data class Product(
    @SerializedName("ProductId")
    val productId: String,
    @SerializedName("ProductName")
    val productName: String,
    @SerializedName("ProductDescription")
    val productDescription: String,
    @SerializedName("BrandName")
    val brandName: String,
    @SerializedName("CategoryName")
    val categoryName: String,
    @SerializedName("UnitPrice")
    val unitPrice: Double,
    @SerializedName("UnitSize")
    val unitSize: String,
    @SerializedName("UnitInStock")
    val unitInStock: Int,
    @SerializedName("isAvailable")
    val isAvailable: Boolean,
    @SerializedName("Pictures")
    val pictures: List<String>,
    @SerializedName("CreatedAt")
    val createdAt: String,
    @SerializedName("UpdatedAt")
    val updatedAt: String
)
