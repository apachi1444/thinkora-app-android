package com.apachi.auraskin.domain.repository

import com.apachi.auraskin.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<Notification>>
    suspend fun insertNotification(notification: Notification)
    suspend fun markAsRead(id: String)
    suspend fun markAllAsRead()
    suspend fun deleteNotification(id: String)
    suspend fun deleteAllNotifications()
}

