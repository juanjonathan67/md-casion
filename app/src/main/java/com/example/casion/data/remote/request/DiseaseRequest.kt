package com.example.casion.data.remote.request

data class DiseaseRequest (
    val category: String,
    val name: String,
    val description: String,
    val suggestion: String,
    val confidenceScore: String,
    val createdAt: String,
)