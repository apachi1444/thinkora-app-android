package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.model.Habit
import com.apachi.thinkora.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(): Flow<List<Habit>> {
        return repository.getAllHabits()
    }
}
