package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.Notification
import com.apachi.auraskin.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<Notification>> {
        return repository.getAllNotifications()
    }
}

