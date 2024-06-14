package com.example.casion.data.repository

import com.example.casion.data.remote.request.ChatRequest
import com.example.casion.data.remote.response.ChatResponse
import com.example.casion.data.remote.response.ErrorResponse
import com.example.casion.data.remote.response.StoreChatResponse
import com.example.casion.data.remote.retrofit.DatabaseApiService
import com.example.casion.data.result.Result
import com.example.casion.util.parseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class DatabaseRepository private constructor(
    private val databaseApiService: DatabaseApiService
){
    suspend fun getChat() : Result<ChatResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val chatResponse = databaseApiService.getChat()
                if (chatResponse.success) {
                    return@withContext Result.Success(chatResponse)
                } else {
                    return@withContext Result.Error(chatResponse.message)
                }
            } catch (e : HttpException) {
                return@withContext Result.Error(parseError(e))
            }
        }
    }

    suspend fun storeChat(
        chatRequest: ChatRequest
    ) : Result<StoreChatResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val storeChatResponse = databaseApiService.storeChat(chatRequest)
                if (storeChatResponse.success) {
                    return@withContext Result.Success(storeChatResponse)
                } else {
                    return@withContext Result.Error(storeChatResponse.message)
                }
            } catch (e : HttpException) {
                return@withContext Result.Error(parseError(e))
            }
        }
    }

    suspend fun deleteChat(
        chatId: String
    ) : Result<ErrorResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val deleteChatResponse = databaseApiService.deleteChat(chatId)
                if (deleteChatResponse.success) {
                    return@withContext Result.Success(deleteChatResponse)
                } else {
                    return@withContext Result.Error(deleteChatResponse.message)
                }
            } catch (e : HttpException) {
                return@withContext Result.Error(parseError(e))
            }
        }
    }

    companion object {
        @Volatile
        private var instance: DatabaseRepository? = null

        fun getInstance(
            databaseApiService: DatabaseApiService
        ) : DatabaseRepository =
            instance ?: synchronized(this) {
                instance ?: DatabaseRepository(databaseApiService)
            }.also { instance = it }
    }
}