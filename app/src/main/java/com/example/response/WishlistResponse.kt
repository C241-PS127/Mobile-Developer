package com.example.response

import com.google.gson.annotations.SerializedName

data class WishlistResponseItem(

	@field:SerializedName("BrandName")
	val brandName: String? = null,

	@field:SerializedName("UnitPrice")
	val unitPrice: Int? = null,

	@field:SerializedName("ProductName")
	val productName: String? = null,

	@field:SerializedName("CreatedAt")
	val createdAt: String? = null,

	@field:SerializedName("FullName")
	val fullName: String? = null,

	@field:SerializedName("ProductId")
	val productId: String? = null,

	@field:SerializedName("CustomerId")
	val customerId: String? = null,

	@field:SerializedName("UpdatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("WishlistId")
	val wishlistId: String? = null
)
