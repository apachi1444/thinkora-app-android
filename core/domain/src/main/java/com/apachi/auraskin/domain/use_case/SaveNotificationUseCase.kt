package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.Notification
import com.apachi.auraskin.domain.repository.NotificationRepository
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: Notification) {
        repository.insertNotification(notification)
    }
}

