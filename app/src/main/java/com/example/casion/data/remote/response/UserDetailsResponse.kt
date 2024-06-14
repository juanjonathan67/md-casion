package com.example.casion.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserDetailsResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("userDetails")
	val userDetails: UserDetails
)

data class UserDetails(

	@field:SerializedName("birthday")
	val birthday: String,

	@field:SerializedName("gender")
	val gender: String,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("email")
	val email: String
)
