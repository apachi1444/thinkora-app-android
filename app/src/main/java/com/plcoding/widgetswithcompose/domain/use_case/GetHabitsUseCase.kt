package com.plcoding.widgetswithcompose.domain.use_case

import com.plcoding.widgetswithcompose.domain.model.Habit
import com.plcoding.widgetswithcompose.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<Habit>> {
        return repository.getAllHabits()
    }
}
