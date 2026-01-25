package com.plcoding.widgetswithcompose.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.widgetswithcompose.domain.model.DailyStreak
import com.plcoding.widgetswithcompose.domain.model.Quote
import com.plcoding.widgetswithcompose.domain.use_case.GetDailyQuoteUseCase
import com.plcoding.widgetswithcompose.domain.use_case.GetDailyStreakUseCase
import com.plcoding.widgetswithcompose.domain.use_case.MarkQuoteAsReadUseCase
import com.plcoding.widgetswithcompose.domain.use_case.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDailyQuoteUseCase: GetDailyQuoteUseCase,
    private val getDailyStreakUseCase: GetDailyStreakUseCase,
    private val markQuoteAsReadUseCase: MarkQuoteAsReadUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getUserNameUseCase: com.plcoding.widgetswithcompose.domain.use_case.GetUserNameUseCase,
    private val getHabitsUseCase: com.plcoding.widgetswithcompose.domain.use_case.GetHabitsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadDailyQuote()
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

    private fun loadDailyQuote() {
        getDailyQuoteUseCase().onEach { quote ->
            _state.value = _state.value.copy(dailyQuote = quote)
        }.launchIn(viewModelScope)
    }

    private fun loadStreak() {
        getDailyStreakUseCase().onEach { streak ->
            _state.value = _state.value.copy(streak = streak)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.MarkAsRead -> {
                viewModelScope.launch {
                    _state.value.dailyQuote?.let {
                        markQuoteAsReadUseCase(it.id)
                    }
                }
            }
            is HomeEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    _state.value.dailyQuote?.let {
                        toggleFavoriteUseCase(it.id)
                    }
                }
            }
        }
    }
}

data class HomeState(
    val dailyQuote: Quote? = null,
    val streak: DailyStreak = DailyStreak(0, 0),
    val userName: String = "",
    val habits: List<com.plcoding.widgetswithcompose.domain.model.Habit> = emptyList()
)

sealed class HomeEvent {
    object MarkAsRead: HomeEvent()
    object ToggleFavorite: HomeEvent()
}
