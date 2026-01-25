package com.plcoding.widgetswithcompose.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.widgetswithcompose.domain.use_case.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<OnboardingUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: OnboardingEvent) {
        when(event) {
            is OnboardingEvent.EnterName -> {
                _state.value = _state.value.copy(name = event.name)
            }
            is OnboardingEvent.ToggleInterest -> {
                val interests = _state.value.selectedInterests.toMutableList()
                if (interests.contains(event.interest)) {
                    interests.remove(event.interest)
                } else {
                    interests.add(event.interest)
                }
                _state.value = _state.value.copy(selectedInterests = interests)
            }
            is OnboardingEvent.Submit -> {
                viewModelScope.launch {
                    completeOnboardingUseCase(
                        name = _state.value.name,
                        interests = _state.value.selectedInterests
                    )
                    _uiEvent.send(OnboardingUiEvent.OnboardingCompleted)
                }
            }
        }
    }
}

data class OnboardingState(
    val name: String = "",
    val availableInterests: List<String> = listOf("Business", "Life", "Sports", "Tech"),
    val selectedInterests: List<String> = emptyList()
)

sealed class OnboardingEvent {
    data class EnterName(val name: String): OnboardingEvent()
    data class ToggleInterest(val interest: String): OnboardingEvent()
    object Submit: OnboardingEvent()
}

sealed class OnboardingUiEvent {
    object OnboardingCompleted: OnboardingUiEvent()
}
