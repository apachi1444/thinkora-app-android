package com.apachi.thinkora.domain.use_case

import com.apachi.thinkora.domain.repository.HabitRepository
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(name: String, initialStreak: Int) {
        if (name.isBlank()) return
        repository.addHabit(name, initialStreak)
    }
}
