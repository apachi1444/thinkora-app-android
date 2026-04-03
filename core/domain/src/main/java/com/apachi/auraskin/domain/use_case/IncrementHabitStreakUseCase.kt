package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.repository.HabitRepository
import javax.inject.Inject

class IncrementHabitStreakUseCase @Inject constructor(
    private val repository: HabitRepository,
    private val checkAchievementsUseCase: CheckAchievementsUseCase
) {
    suspend operator fun invoke(id: String) {
        repository.incrementHabitStreak(id)
        checkAchievementsUseCase()
    }
}
