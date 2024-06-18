package com.example.data.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponseItem(

    @field:SerializedName("Email") val email: String? = null,

    @field:SerializedName("Address") val address: String? = null,

    @field:SerializedName("Picture") val picture: String? = null,

    @field:SerializedName("Phone") val phone: String? = null,

    @field:SerializedName("FullName") val fullName: String? = null,

    @field:SerializedName("isBrand") val isBrand: Boolean? = null
)
