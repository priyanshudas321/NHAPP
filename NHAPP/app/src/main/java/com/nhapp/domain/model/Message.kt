package com.nhapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    @SerialName("chat_id")
    val chatId: String,
    @SerialName("sender_id")
    val senderId: String,
    val content: String,
    @SerialName("type")
    val type: String = "text",
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("read_at")
    val readAt: String? = null
)
