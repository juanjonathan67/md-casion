package com.example.casion.data.remote.retrofit

import com.example.casion.data.remote.response.PredictResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PredictApiService {
    @FormUrlEncoded
    @POST("predict")
    suspend fun predict(
        @Field("category") category: String,
        @Field("text") text: String,
    ) : PredictResponse
}