package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.repository.HabitRepository
import javax.inject.Inject

class IncrementHabitStreakUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(id: String) {
        repository.incrementHabitStreak(id)
    }
}
