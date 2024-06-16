package com.example.response

import com.google.gson.annotations.SerializedName

data class Shippers(

	@field:SerializedName("shippers")
	val shippers: List<ShippersItem?>? = null
)

data class ShippersItem(

	@field:SerializedName("CompanyName")
	val companyName: String? = null,

	@field:SerializedName("Phone")
	val phone: String? = null,

	@field:SerializedName("ShipperId")
	val shipperId: String
)
