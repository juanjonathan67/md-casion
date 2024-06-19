package com.example.casion.data.remote.retrofit

import com.example.casion.data.remote.request.ChatRequest
import com.example.casion.data.remote.request.DiseaseRequest
import com.example.casion.data.remote.response.ChatResponse
import com.example.casion.data.remote.response.DiseaseResponse
import com.example.casion.data.remote.response.ErrorResponse
import com.example.casion.data.remote.response.StoreChatResponse
import com.example.casion.data.remote.response.StoreDiseaseResponse
import com.example.casion.data.remote.response.UserDetailsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DatabaseApiService {
    @GET("auth/details")
    suspend fun getUserDetails(): UserDetailsResponse

    @GET("chat/")
    suspend fun getChat() : ChatResponse

    @GET("disease/")
    suspend fun getDiseases() : DiseaseResponse

    @POST("chat/")
    suspend fun storeChat(@Body chatRequest: ChatRequest) : StoreChatResponse

    @POST("disease/")
    suspend fun storeDisease(@Body diseaseRequest: DiseaseRequest) : StoreDiseaseResponse

    @PUT("chat/{chatId}")
    suspend fun updateChat(@Path("chatId") chatId: String, @Body chatRequest: ChatRequest) : ErrorResponse

    @DELETE("chat/{chatId}")
    suspend fun deleteChat(@Path("chatId") chatId: String) : ErrorResponse
}