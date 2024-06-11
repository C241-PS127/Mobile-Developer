package com.example.response

import com.google.gson.annotations.SerializedName

data class AddCartResponse(
	@field:SerializedName("message")
	val message: String? = null,
)
data class CartResponseItem(

	@field:SerializedName("BrandName")
	val brandName: String? = null,

	@field:SerializedName("UnitPrice")
	val unitPrice: Int? = null,

	@field:SerializedName("ProductName")
	val productName: String? = null,

	@field:SerializedName("IsActive")
	val isActive: Boolean? = null,

	@field:SerializedName("CreatedAt")
	val createdAt: String? = null,

	@field:SerializedName("FullName")
	val fullName: String? = null,

	@field:SerializedName("CartId")
	val cartId: String? = null,

	@field:SerializedName("ProductId")
	val productId: String? = null,

	@field:SerializedName("CustomerId")
	val customerId: String? = null,

	@field:SerializedName("Count")
	val count: Int? = null,

	@field:SerializedName("UpdatedAt")
	val updatedAt: String? = null
)
