package com.example.casion.data.repository

import com.example.casion.data.remote.request.ChatRequest
import com.example.casion.data.remote.request.DiseaseRequest
import com.example.casion.data.remote.response.ChatResponse
import com.example.casion.data.remote.response.DiseaseResponse
import com.example.casion.data.remote.response.ErrorResponse
import com.example.casion.data.remote.response.StoreChatResponse
import com.example.casion.data.remote.response.StoreDiseaseResponse
import com.example.casion.data.remote.response.UserDetailsResponse
import com.example.casion.data.remote.retrofit.DatabaseApiService
import com.example.casion.data.result.Result
import com.example.casion.util.parseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository private constructor(
    private val databaseApiService: DatabaseApiService
){
    suspend fun getUserDetails() : Result<UserDetailsResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val userDetailsResponse = databaseApiService.getUserDetails()
                if (userDetailsResponse.success) {
                    return@withContext Result.Success(userDetailsResponse)
                } else {
                    return@withContext Result.Error(userDetailsResponse.message)
                }
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
            }
        }
    }

    suspend fun getChat() : Result<ChatResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val chatResponse = databaseApiService.getChat()
                if (chatResponse.success) {
                    return@withContext Result.Success(chatResponse)
                } else {
                    return@withContext Result.Error(chatResponse.message)
                }
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
            }
        }
    }

    suspend fun getDisease() : Result<DiseaseResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val diseaseResponse = databaseApiService.getDiseases()
                if (diseaseResponse.success) {
                    return@withContext Result.Success(diseaseResponse)
                } else {
                    return@withContext Result.Error(diseaseResponse.message)
                }
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
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
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
            }
        }
    }

    suspend fun storeDisease(
        diseaseRequest: DiseaseRequest
    ) : Result<StoreDiseaseResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val storeDiseaseResponse = databaseApiService.storeDisease(diseaseRequest)
                if (storeDiseaseResponse.success) {
                    return@withContext Result.Success(storeDiseaseResponse)
                } else {
                    return@withContext Result.Error(storeDiseaseResponse.message)
                }
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
            }
        }
    }

    suspend fun updateChat(
        chatId: String,
        chatRequest: ChatRequest
    ) : Result<ErrorResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val updateChatResponse = databaseApiService.updateChat(chatId, chatRequest)
                if (updateChatResponse.success) {
                    return@withContext Result.Success(updateChatResponse)
                } else {
                    return@withContext Result.Error(updateChatResponse.message)
                }
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
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
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
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