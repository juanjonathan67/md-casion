package com.example.casion.data.remote.retrofit

import com.example.casion.data.remote.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("full_name") fullName: String,
        @Field("email") email: String,
        @Field("age") age: String,
        @Field("gender") gender: Boolean,
        @Field("password") password: String,
    ) : LoginResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ) : LoginResponse

    @POST("auth/validate")
    suspend fun validate(
        @Header("Authorization") token: String,
    ) : LoginResponse
}