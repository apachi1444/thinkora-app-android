package com.apachi.thinkora.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.thinkora.data.local.entity.NotificationEntity
import com.apachi.thinkora.domain.use_case.GetNotificationsUseCase
import com.apachi.thinkora.domain.use_case.MarkAllNotificationsReadUseCase
import com.apachi.thinkora.domain.use_case.MarkNotificationReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationReadUseCase: MarkNotificationReadUseCase,
    private val markAllNotificationsReadUseCase: MarkAllNotificationsReadUseCase
) : ViewModel() {

    val notifications: StateFlow<List<NotificationEntity>> = getNotificationsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun markAsRead(id: String) {
        viewModelScope.launch {
            markNotificationReadUseCase(id)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            markAllNotificationsReadUseCase()
        }
    }
}
