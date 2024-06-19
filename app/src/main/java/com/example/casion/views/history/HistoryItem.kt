package com.example.casion.views.history

import com.example.casion.data.remote.response.ChatsItem
import com.example.casion.data.remote.response.DiseasesItem

data class ChatHistoryItem (
    var chats: List<ChatsItem>,

    var date: String,
)

data class MedCheckHistoryItem (
    var diseasesItem: List<DiseasesItem>,

    var date: String,
)