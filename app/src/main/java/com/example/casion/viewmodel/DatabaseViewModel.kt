package com.example.casion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casion.data.remote.request.ChatRequest
import com.example.casion.data.remote.response.StoreChatResponse
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

    fun storeChat(chatRequest: ChatRequest) : LiveData<Result<StoreChatResponse>> {
        val result: MutableLiveData<Result<StoreChatResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = databaseRepository?.storeChat(chatRequest)
        }
        return result
    }
}