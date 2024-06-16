package com.example.casion.data.repository

import com.example.casion.data.remote.response.LoginResponse
import com.example.casion.data.remote.retrofit.AuthApiService
import com.example.casion.util.UserPreferences
import com.example.casion.data.result.Result
import com.example.casion.util.parseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository private constructor(
    private val authApiService: AuthApiService,
    private val prefs: UserPreferences
) {
    suspend fun register(
        fullName: String,
        email: String,
        birthday: String,
        gender: Boolean,
        password: String,
    ) : Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val registerResponse = authApiService.register(fullName, email, birthday, gender, password)
                if (registerResponse.success) {
                    prefs.saveUserToken(registerResponse.token)
                    return@withContext Result.Success(registerResponse)
                } else {
                    return@withContext Result.Error(registerResponse.message)
                }
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
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
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
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
            } catch (e : Exception) {
                return@withContext Result.Error(parseException(e))
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