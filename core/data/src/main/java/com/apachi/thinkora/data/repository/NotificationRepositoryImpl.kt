package com.apachi.thinkora.data.repository

import com.apachi.thinkora.data.local.dao.NotificationDao
import com.apachi.thinkora.data.local.entity.NotificationEntity
import com.apachi.thinkora.domain.model.Notification
import com.apachi.thinkora.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao
) : NotificationRepository {
    override fun getAllNotifications(): Flow<List<Notification>> {
        return dao.getAllNotifications().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertNotification(notification: Notification) {
        dao.insertNotification(notification.toEntity())
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

    override suspend fun deleteAllNotifications() {
        dao.deleteAllNotifications()
    }
}

fun NotificationEntity.toDomain(): Notification {
    return Notification(
        id = id,
        title = title,
        message = message,
        timestamp = timestamp,
        isRead = isRead,
        type = type
    )
}

fun Notification.toEntity(): NotificationEntity {
    return NotificationEntity(
        id = id,
        title = title,
        message = message,
        timestamp = timestamp,
        isRead = isRead,
        type = type
    )
}

