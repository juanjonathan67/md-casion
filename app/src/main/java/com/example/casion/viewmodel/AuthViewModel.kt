package com.example.casion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casion.data.remote.response.LoginResponse
import com.example.casion.data.repository.AuthRepository
import com.example.casion.data.result.Result
import kotlinx.coroutines.launch

class AuthViewModel (private val authRepository: AuthRepository?) : ViewModel() {

    fun register(
        fullName: String,
        email: String,
        age: String,
        gender: Boolean,
        password: String,
    ) : LiveData<Result<LoginResponse>> {
        val result: MutableLiveData<Result<LoginResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = authRepository?.register(fullName, email, age, gender, password)
        }
        return result
    }

    fun login(
        email: String,
        password: String,
    ) : LiveData<Result<LoginResponse>> {
        val result: MutableLiveData<Result<LoginResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = authRepository?.login(email, password)
        }
        return result
    }

    fun validate (
        token: String,
    ) : LiveData<Result<LoginResponse>> {
        val result: MutableLiveData<Result<LoginResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = authRepository?.validate(token)
        }
        return result
    }
}