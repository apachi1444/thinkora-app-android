package com.plcoding.widgetswithcompose.domain.use_case

import com.plcoding.widgetswithcompose.domain.repository.HabitRepository
import javax.inject.Inject

class IncrementHabitStreakUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(id: String) {
        repository.incrementHabitStreak(id)
    }
}
