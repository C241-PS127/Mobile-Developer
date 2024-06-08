package com.example.response

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("CategoryResponse")
	val categoryResponse: List<CategoryResponseItem?>? = null
)

data class CategoryResponseItem(

	@field:SerializedName("CategoryId")
	val categoryId: String? = null,

	@field:SerializedName("Description")
	val description: String? = null,

	@field:SerializedName("CreatedAt")
	val createdAt: String? = null,

	@field:SerializedName("CategoryName")
	val categoryName: String? = null,

	@field:SerializedName("UpdatedAt")
	val updatedAt: String? = null
)
