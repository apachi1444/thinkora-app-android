package com.apachi.thinkora.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.thinkora.domain.model.DailyStreak
import com.apachi.thinkora.domain.model.Habit
import com.apachi.thinkora.domain.use_case.GetDailyStreakUseCase
import com.apachi.thinkora.domain.use_case.GetHabitsUseCase
import com.apachi.thinkora.domain.use_case.GetUserNameUseCase
import com.apachi.thinkora.domain.use_case.IncrementHabitStreakUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDailyStreakUseCase: GetDailyStreakUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getHabitsUseCase: GetHabitsUseCase,
    private val incrementHabitStreakUseCase: IncrementHabitStreakUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadStreak()
        loadUserName()
    }

    private fun loadUserName() {
        getUserNameUseCase().onEach { name ->
            _state.value = _state.value.copy(userName = name)
        }.launchIn(viewModelScope)
        getHabitsUseCase().onEach { habits ->
            _state.value = _state.value.copy(habits = habits)
        }.launchIn(viewModelScope)
    }

    private fun loadStreak() {
        getDailyStreakUseCase().onEach { streak ->
            _state.value = _state.value.copy(streak = streak)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.IncrementHabitStreak -> {
                viewModelScope.launch {
                    incrementHabitStreakUseCase(event.habitId)
                }
            }
        }
    }
}

data class HomeState(
    val streak: DailyStreak = DailyStreak(0, 0),
    val userName: String = "",
    val habits: List<Habit> = emptyList()
)

sealed class HomeEvent {
    data class IncrementHabitStreak(val habitId: String) : HomeEvent()
}
