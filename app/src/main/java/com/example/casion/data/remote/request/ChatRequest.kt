package com.example.casion.data.remote.request

data class ChatRequest(
    val dateTime: String,

    val messages: List<MessagesItem>,

    val title: String
)

data class MessagesItem(

    val bot: Boolean,

    val time: String,

    val message: String
)
