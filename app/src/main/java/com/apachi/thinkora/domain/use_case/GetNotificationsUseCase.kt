package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.data.local.entity.NotificationEntity
import com.apachi.thinkora.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    operator fun invoke(): Flow<List<NotificationEntity>> {
        return repository.getAllNotifications()
    }
}
