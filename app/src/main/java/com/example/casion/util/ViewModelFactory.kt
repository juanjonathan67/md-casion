package com.example.casion.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.casion.data.repository.AuthRepository
import com.example.casion.data.repository.DatabaseRepository
import com.example.casion.data.repository.PredictRepository
import com.example.casion.di.Injection
import com.example.casion.viewmodel.AuthViewModel

class ViewModelFactory private constructor(
    private val chatRepository: PredictRepository? = null,
    private val authRepository: AuthRepository? = null,
    private val databaseRepository: DatabaseRepository? = null
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
//        ChatViewModel::class.java -> ChatViewModel(chatRepository)
        AuthViewModel::class.java -> AuthViewModel(authRepository)
//        DatabaseViewModel::class.java -> DatabaseViewModel(databaseRepository)
        else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    } as T

    companion object {
        @Volatile
        private var chatInstance: ViewModelFactory? = null

        @Volatile
        private var authInstance: ViewModelFactory? = null

        @Volatile
        private var databaseInstance: ViewModelFactory? = null

        fun getChatInstance(context: Context): ViewModelFactory =
            chatInstance ?: synchronized(this) {
                chatInstance ?: ViewModelFactory(chatRepository = Injection.providePredictRepository(context))
            }.also { chatInstance = it }

        fun getAuthInstance(context: Context): ViewModelFactory =
            authInstance ?: synchronized(this) {
                authInstance ?: ViewModelFactory(authRepository = Injection.provideAuthRepository(context))
            }

        fun getDatabaseInstance(context: Context): ViewModelFactory =
            authInstance ?: synchronized(this) {
                authInstance ?: ViewModelFactory(databaseRepository = Injection.provideDatabaseRepository(context))
            }
    }
}