package com.example.casion.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatResponse(

    @field:SerializedName("chats")
    val chats: List<ChatsItem>,

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable

@Parcelize
data class ChatsItem(

    @field:SerializedName("dateTime")
    val dateTime: String,

    @field:SerializedName("chatId")
    val chatId: String,

    @field:SerializedName("messages")
    val messages: List<MessagesItem>,

    @field:SerializedName("title")
    val title: String
) : Parcelable

@Parcelize
data class MessagesItem(

    @field:SerializedName("bot")
    val bot: Boolean,

    @field:SerializedName("messageId")
    val messageId: String,

    @field:SerializedName("time")
    val time: String,

    @field:SerializedName("message")
    val message: String
) : Parcelable
