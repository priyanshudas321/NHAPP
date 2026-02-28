package com.nhapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class ChatMember(
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("user_id")
    val userId: String
)
