package com.example.casion.util

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import com.example.casion.data.remote.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.regex.Pattern

private val PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&+=;'])(?=\\\\S+\$).{8,}\$")

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun parseError(e: HttpException) : String {
    val errorBody = e.response()?.errorBody()?.toString()
    val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
    return errorResponse.message
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPassword() = !isNullOrEmpty() && PASSWORD.matcher(this).matches()

fun CharSequence?.isValidInteger() = !isNullOrEmpty()