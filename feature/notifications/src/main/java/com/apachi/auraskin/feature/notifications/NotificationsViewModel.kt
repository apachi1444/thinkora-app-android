package com.apachi.auraskin.feature.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.auraskin.domain.model.Notification
import com.apachi.auraskin.domain.use_case.GetNotificationsUseCase
import com.apachi.auraskin.domain.use_case.MarkAllNotificationsReadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val markAllNotificationsReadUseCase: MarkAllNotificationsReadUseCase
) : ViewModel() {

    val notifications: StateFlow<List<Notification>> = getNotificationsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun markAllAsRead() {
        viewModelScope.launch {
            markAllNotificationsReadUseCase()
        }
    }
}
