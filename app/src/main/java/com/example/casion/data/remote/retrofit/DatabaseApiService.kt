package com.example.casion.data.remote.retrofit

import com.example.casion.data.remote.request.ChatRequest
import com.example.casion.data.remote.response.ChatResponse
import com.example.casion.data.remote.response.ErrorResponse
import com.example.casion.data.remote.response.StoreChatResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DatabaseApiService {
    @GET("chat/")
    suspend fun getChat() : ChatResponse

    @POST("chat/")
    suspend fun storeChat(@Body chatRequest: ChatRequest) : StoreChatResponse

    @DELETE("chat/{chatId}")
    suspend fun deleteChat(@Path("chatId") chatId: String) : ErrorResponse
}