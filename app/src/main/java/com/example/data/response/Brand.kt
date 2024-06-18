package com.example.data.response

import com.google.gson.annotations.SerializedName

data class Brand(
    @SerializedName("BrandId") val brandId: String,
    @SerializedName("BrandName") val brandName: String,
    @SerializedName("Address") val address: String,
    @SerializedName("Logo") val logo: String,
    @SerializedName("CreatedAt") val createdAt: String,
    @SerializedName("UpdatedAt") val updatedAt: String,
    @SerializedName("LogoUrl") val logoUrl: String
)
