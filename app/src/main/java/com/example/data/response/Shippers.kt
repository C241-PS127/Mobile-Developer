package com.example.data.response

import com.google.gson.annotations.SerializedName

data class ShippersItem(

    @field:SerializedName("CompanyName") val companyName: String? = null,

    @field:SerializedName("Phone") val phone: String? = null,

    @field:SerializedName("ShipperId") val shipperId: String
)
