package com.example.response

import com.google.gson.annotations.SerializedName

data class Products(

	@field:SerializedName("Products")
	val products: List<ProductsItem?>? = null
)

data class ProductsItem(

	@field:SerializedName("CategoryId")
	val categoryId: String? = null,

	@field:SerializedName("isAvailable")
	val isAvailable: Boolean? = null,

	@field:SerializedName("ProductName")
	val productName: String? = null,

	@field:SerializedName("CreatedAt")
	val createdAt: String? = null,

	@field:SerializedName("ProductId")
	val productId: String? = null,

	@field:SerializedName("BrandId")
	val brandId: String? = null,

	@field:SerializedName("UnitSize")
	val unitSize: String? = null,

	@field:SerializedName("UpdatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("ImgUrl")
	val imgUrl: String? = null,

	@field:SerializedName("BrandName")
	val brandName: String? = null,

	@field:SerializedName("UnitPrice")
	val unitPrice: Int? = null,

	@field:SerializedName("UnitInStock")
	val unitInStock: Int? = null,

	@field:SerializedName("Picture")
	val picture: String? = null,

	@field:SerializedName("CategoryName")
	val categoryName: String? = null,

	@field:SerializedName("ProductDescription")
	val productDescription: String? = null
)
