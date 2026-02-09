package com.apachi.thinkora.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkThemeConfig: Flow<Boolean>
    suspend fun setDarkThemeConfig(isDarkTheme: Boolean)
    
    val languageCode: Flow<String>
    suspend fun setLanguageCode(code: String)
}
