package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.data.local.entity.NotificationEntity
import com.apachi.thinkora.domain.repository.NotificationRepository
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: NotificationEntity) {
        repository.insertNotification(notification)
    }
}
