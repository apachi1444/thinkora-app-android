package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: String) {
        repository.markAsRead(id)
    }
}
