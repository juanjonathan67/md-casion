package com.example.casion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casion.data.remote.request.ChatRequest
import com.example.casion.data.remote.request.DiseaseRequest
import com.example.casion.data.remote.response.ChatResponse
import com.example.casion.data.remote.response.DiseaseResponse
import com.example.casion.data.remote.response.ErrorResponse
import com.example.casion.data.remote.response.StoreChatResponse
import com.example.casion.data.remote.response.StoreDiseaseResponse
import com.example.casion.data.remote.response.UserDetailsResponse
import com.example.casion.data.repository.DatabaseRepository
import com.example.casion.data.result.Result
import kotlinx.coroutines.launch

class DatabaseViewModel (private val databaseRepository: DatabaseRepository?) : ViewModel() {

    fun getUserDetails() : LiveData<Result<UserDetailsResponse>> {
        val result: MutableLiveData<Result<UserDetailsResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = databaseRepository?.getUserDetails()
        }
        return result
    }

    fun getChatHistory() : LiveData<Result<ChatResponse>> {
        val result: MutableLiveData<Result<ChatResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = databaseRepository?.getChat()
        }
        return result
    }

    fun getDiseaseHistory() : LiveData<Result<DiseaseResponse>> {
        val result: MutableLiveData<Result<DiseaseResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = databaseRepository?.getDisease()
        }
        return result
    }

    fun storeChat(chatRequest: ChatRequest) : LiveData<Result<StoreChatResponse>> {
        val result: MutableLiveData<Result<StoreChatResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = databaseRepository?.storeChat(chatRequest)
        }
        return result
    }

    fun storeDisease(diseaseRequest: DiseaseRequest) : LiveData<Result<StoreDiseaseResponse>> {
        val result: MutableLiveData<Result<StoreDiseaseResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = databaseRepository?.storeDisease(diseaseRequest)
        }
        return result
    }

    fun updateChat(chatId: String, chatRequest: ChatRequest) : LiveData<Result<ErrorResponse>> {
        val result: MutableLiveData<Result<ErrorResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = databaseRepository?.updateChat(chatId, chatRequest)
        }
        return result
    }
}