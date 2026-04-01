package com.apachi.thinkora.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkThemeConfig: Flow<Boolean>
    suspend fun setDarkThemeConfig(isDarkTheme: Boolean)

    val languageCode: Flow<String>
    suspend fun setLanguageCode(code: String)

    val notificationsEnabled: Flow<Boolean>
    val reminderHour: Flow<Int>
    val reminderMinute: Flow<Int>
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setReminderTime(hour: Int, minute: Int)
}
