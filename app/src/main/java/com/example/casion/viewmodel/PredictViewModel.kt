package com.example.casion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.casion.data.remote.response.PredictResponse
import com.example.casion.data.repository.PredictRepository
import com.example.casion.data.result.Result
import kotlinx.coroutines.launch

class PredictViewModel (private val predictRepository: PredictRepository?) : ViewModel() {
    fun predict (category: String, text: ArrayList<String>) : LiveData<Result<PredictResponse>> {
        val result: MutableLiveData<Result<PredictResponse>> = MutableLiveData(Result.Loading)
        viewModelScope.launch {
            result.value = predictRepository?.predict(category, text)
        }
        return result
    }
}