package com.example.casion.util

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.casion.data.remote.response.ErrorResponse
import com.example.casion.data.result.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.regex.Pattern

private val PASSWORD = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&+=;'])(?=\\\\S+\$).{8,}\$")

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun parseException(e: Exception) : String {
    when (e) {
        is HttpException -> {
            if (e.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return "Harap login kembali"
            }
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            return errorResponse.message
        }
        is SocketTimeoutException -> { return "Koneksi gagal" }
        else -> { return e.message.toString() }
    }
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isValidPassword() = !isNullOrEmpty() && PASSWORD.matcher(this).matches()

fun String.capitalizeWords(): String = split(" ").map { it.replaceFirstChar { it2 -> it2.uppercaseChar() } }.joinToString(" ")
