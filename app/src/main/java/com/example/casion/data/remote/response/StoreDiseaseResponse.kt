package com.example.casion.data.remote.response

import com.google.gson.annotations.SerializedName

data class StoreDiseaseResponse(

    @field:SerializedName("chatId")
    val diseaseId: String,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
)
