package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteHabit(id)
    }
}
