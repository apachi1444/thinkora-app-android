package com.apachi.thinkora.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.thinkora.core.domain.repository.SettingsRepository
import com.apachi.thinkora.domain.use_case.DeleteUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val deleteUserDataUseCase: DeleteUserDataUseCase
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = settingsRepository.isDarkThemeConfig
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkThemeConfig(isDark)
        }
    }

    val languageCode: StateFlow<String> = settingsRepository.languageCode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    fun setLanguage(code: String) {
        viewModelScope.launch {
            settingsRepository.setLanguageCode(code)
        }
    }

    fun deleteUserDataLocally() {
        viewModelScope.launch {
            deleteUserDataUseCase()
        }
    }
}
