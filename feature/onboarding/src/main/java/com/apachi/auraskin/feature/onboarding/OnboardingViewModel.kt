package com.apachi.auraskin.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.auraskin.core.domain.repository.SettingsRepository
import com.apachi.auraskin.domain.scheduler.HabitReminderScheduler
import com.apachi.auraskin.domain.use_case.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val settingsRepository: SettingsRepository,
    private val habitReminderScheduler: HabitReminderScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<OnboardingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.EnterName -> {
                _state.value = _state.value.copy(name = event.name)
            }
            is OnboardingEvent.SelectSkinFocus -> {
                _state.value = _state.value.copy(skinFocus = event.focus)
            }
            is OnboardingEvent.SelectSkinType -> {
                _state.value = _state.value.copy(skinType = event.type)
            }
            is OnboardingEvent.SelectRoutineType -> {
                _state.value = _state.value.copy(routineType = event.routine)
            }
            is OnboardingEvent.NextPage -> {
                _state.value = _state.value.copy(currentPage = _state.value.currentPage + 1)
            }
            is OnboardingEvent.PreviousPage -> {
                if (_state.value.currentPage > 0) {
                    _state.value = _state.value.copy(currentPage = _state.value.currentPage - 1)
                }
            }
            is OnboardingEvent.SubmitWithNotifications -> {
                viewModelScope.launch {
                    settingsRepository.setNotificationsEnabled(event.enabled)
                    if (event.enabled) {
                        settingsRepository.setReminderTime(event.hour, event.minute)
                        habitReminderScheduler.schedule(event.enabled, event.hour, event.minute)
                    }
                    completeOnboardingUseCase(
                        name = _state.value.name,
                        skinFocus = _state.value.skinFocus,
                        skinType = _state.value.skinType,
                        routineType = _state.value.routineType
                    )
                    _uiEvent.send(OnboardingUiEvent.OnboardingCompleted)
                }
            }
        }
    }
}

data class OnboardingState(
    val currentPage: Int = 0,
    val name: String = "",
    val skinFocus: String = "", // e.g. "Clear Breakouts", "Reduce Redness", "Build a Routine"
    val skinType: String = "",  // e.g. "Oily", "Dry", "Combination", "Normal"
    val routineType: String = "" // e.g. "None", "Basic", "Advanced"
)

sealed class OnboardingEvent {
    data class EnterName(val name: String) : OnboardingEvent()
    data class SelectSkinFocus(val focus: String) : OnboardingEvent()
    data class SelectSkinType(val type: String) : OnboardingEvent()
    data class SelectRoutineType(val routine: String) : OnboardingEvent()
    object NextPage : OnboardingEvent()
    object PreviousPage : OnboardingEvent()
    data class SubmitWithNotifications(val enabled: Boolean, val hour: Int = 20, val minute: Int = 0) : OnboardingEvent()
}

sealed class OnboardingUiEvent {
    object OnboardingCompleted: OnboardingUiEvent()
    data class NavigateTo(val route: String): OnboardingUiEvent()
}
