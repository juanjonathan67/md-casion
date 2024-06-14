package com.example.casion.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Double,

	@field:SerializedName("suggetion")
	val suggestion: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: String
)
