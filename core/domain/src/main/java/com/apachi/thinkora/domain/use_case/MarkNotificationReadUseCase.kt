package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: String) {
        repository.markAsRead(id)
    }
}
