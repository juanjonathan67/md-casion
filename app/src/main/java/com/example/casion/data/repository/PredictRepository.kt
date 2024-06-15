package com.example.casion.data.repository

import com.example.casion.data.remote.response.PredictResponse
import com.example.casion.data.remote.retrofit.PredictApiService
import com.example.casion.data.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PredictRepository private constructor(
    private val predictApiService: PredictApiService,
){
    suspend fun predict (
        category: String,
        symptoms: ArrayList<String>,
    ) : Result<PredictResponse> {
        return withContext(Dispatchers.IO) {
            val symptomsArray = StringBuilder()
            for (symptom in symptoms) {
                if (symptom == symptoms.last()) {
                    symptomsArray.append(symptom)
                } else {
                    symptomsArray.append("${symptom},")
                }
            }
            try {
                return@withContext Result.Success(predictApiService.predict(category, symptomsArray.toString()))
            } catch (e : HttpException) {
                return@withContext Result.Error(e.message.toString())
            }
        }
    }

    companion object {
        @Volatile
        private var instance: PredictRepository? = null

        fun getInstance(
            predictApiService: PredictApiService
        ): PredictRepository =
            instance ?: synchronized(this) {
                instance ?: PredictRepository(predictApiService)
            }.also { instance = it }
    }
}