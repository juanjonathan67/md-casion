package com.example.casion.data.remote.response

import com.google.gson.annotations.SerializedName

data class DiseaseResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("diseases")
	val diseases: List<DiseasesItem>,

	@field:SerializedName("message")
	val message: String
)

data class DiseasesItem(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("confidenceScore")
	val confidenceScore: String,

	@field:SerializedName("suggestion")
	val suggestion: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("diseaseId")
	val diseaseId: String
)
