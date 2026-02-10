package com.apachi.thinkora.domain.model

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean,
    val type: String
)
