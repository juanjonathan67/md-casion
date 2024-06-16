package com.example.casion.views.history

import com.example.casion.data.remote.response.ChatsItem

data class HistoryItem (
    var chats: List<ChatsItem>,

    var date: String,
)