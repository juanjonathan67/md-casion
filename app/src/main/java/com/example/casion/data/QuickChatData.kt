package com.example.casion.data

data class QuickChatData(
    val title: String,
    val content: List<String>,
    var isExpand: Boolean = false,
)