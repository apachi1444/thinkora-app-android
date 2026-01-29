package com.apachi.thinkora.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apachi.thinkora.domain.model.Habit
import com.apachi.thinkora.domain.use_case.AddHabitUseCase
import com.apachi.thinkora.domain.use_case.DeleteHabitUseCase
import com.apachi.thinkora.domain.use_case.GetHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val addHabitUseCase: AddHabitUseCase,
    private val incrementHabitStreakUseCase: com.apachi.thinkora.domain.use_case.IncrementHabitStreakUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HabitsState())
    val state = _state.asStateFlow()

    init {
        loadHabits()
    }

    private fun loadHabits() {
        getHabitsUseCase().onEach { habits ->
            _state.value = _state.value.copy(habits = habits)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HabitsEvent) {
        when(event) {
            is HabitsEvent.AddHabit -> {
                viewModelScope.launch {
                    addHabitUseCase(event.name, event.initialStreak)
                }
            }
            is HabitsEvent.ShowAddDialog -> {
                _state.value = _state.value.copy(isAddDialogVisible = true)
            }
            is HabitsEvent.HideAddDialog -> {
                _state.value = _state.value.copy(isAddDialogVisible = false)
            }
            is HabitsEvent.IncrementStreak -> {
                viewModelScope.launch {
                    incrementHabitStreakUseCase(event.habitId)
                }
            }
            is HabitsEvent.DeleteHabit -> {
                viewModelScope.launch {
                    deleteHabitUseCase(event.habitId)
                }
            }
        }
    }
}

data class HabitsState(
    val habits: List<Habit> = emptyList(),
    val isAddDialogVisible: Boolean = false
)

sealed class HabitsEvent {
    data class AddHabit(val name: String, val initialStreak: Int) : HabitsEvent()
    object ShowAddDialog : HabitsEvent()
    object HideAddDialog : HabitsEvent()
    data class IncrementStreak(val habitId: String) : HabitsEvent()
    data class DeleteHabit(val habitId: String) : HabitsEvent()
}
