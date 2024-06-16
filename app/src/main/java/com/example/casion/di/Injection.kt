package com.example.casion.di

import android.content.Context
import com.example.casion.data.remote.retrofit.ApiConfig
import com.example.casion.data.repository.AuthRepository
import com.example.casion.data.repository.DatabaseRepository
import com.example.casion.data.repository.PredictRepository
import com.example.casion.util.UserPreferences
import com.example.casion.util.datastore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun providePredictRepository(context: Context) : PredictRepository {
        val apiService = ApiConfig.getPredictApiService()
        return PredictRepository.getInstance(apiService)
    }

    fun provideAuthRepository(context: Context) : AuthRepository {
        val prefs = UserPreferences.getInstance(context.datastore)
        val apiService = ApiConfig.getAuthApiService()
        return AuthRepository.getInstance(apiService, prefs)
    }

    fun provideDatabaseRepository(context: Context) : DatabaseRepository {
        val prefs = UserPreferences.getInstance(context.datastore)
        val token = runBlocking { prefs.getUserToken().first() }
        val apiService = ApiConfig.getDatabaseApiService(token)
        return DatabaseRepository.getInstance(apiService)
    }
}