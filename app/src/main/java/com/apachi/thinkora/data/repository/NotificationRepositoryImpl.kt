package com.apachi.thinkora.data.repository

import com.apachi.thinkora.data.local.dao.NotificationDao
import com.apachi.thinkora.data.local.entity.NotificationEntity
import com.apachi.thinkora.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao
) : NotificationRepository {
    override fun getAllNotifications(): Flow<List<NotificationEntity>> {
        return dao.getAllNotifications()
    }

    override suspend fun insertNotification(notification: NotificationEntity) {
        dao.insertNotification(notification)
    }

    override suspend fun markAsRead(id: String) {
        dao.markAsRead(id)
    }

    override suspend fun markAllAsRead() {
        dao.markAllAsRead()
    }

    override suspend fun deleteNotification(id: String) {
        dao.deleteNotification(id)
    }
}
