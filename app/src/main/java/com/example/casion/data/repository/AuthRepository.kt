package com.example.casion.data.repository

import com.example.casion.data.remote.response.LoginResponse
import com.example.casion.data.remote.retrofit.AuthApiService
import com.example.casion.util.UserPreferences
import com.example.casion.data.result.Result
import com.example.casion.util.parseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthRepository private constructor(
    private val authApiService: AuthApiService,
    private val prefs: UserPreferences
) {
    suspend fun register(
        fullName: String,
        email: String,
        age: String,
        gender: Boolean,
        password: String,
    ) : Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val registerResponse = authApiService.register(fullName, email, age, gender, password)
                if (registerResponse.success) {
                    prefs.saveUserToken(registerResponse.token)
                    return@withContext Result.Success(registerResponse)
                } else {
                    return@withContext Result.Error(registerResponse.message)
                }
            } catch (e : HttpException) {
                return@withContext Result.Error(parseError(e))
            }
        }
    }

    suspend fun login(
        email: String,
        password: String,
    ) : Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val loginResponse = authApiService.login(email, password)
                if (loginResponse.success) {
                    prefs.saveUserToken(loginResponse.token)
                    return@withContext Result.Success(loginResponse)
                } else {
                    return@withContext Result.Error(loginResponse.message)
                }
            } catch (e : HttpException) {
                return@withContext Result.Error(parseError(e))
            }
        }
    }

    suspend fun validate (
        token: String,
    ) : Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApiService.validate("Bearer $token")
                if (response.success) {
                    return@withContext Result.Success(response)
                } else {
                    return@withContext Result.Error(response.message)
                }
            } catch (e: HttpException) {
                return@withContext Result.Error(e.message ?: "Error Occurred")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(
            authApiService: AuthApiService,
            prefs: UserPreferences
        ) : AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(authApiService, prefs)
            }.also { instance = it }
    }
}