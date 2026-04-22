package com.apachi.auraskin.domain.use_case

import com.apachi.auraskin.domain.model.Habit
import com.apachi.auraskin.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHabitsUseCase @Inject constructor(
    private val repository: HabitRepository
) {
    operator fun invoke(query: String): Flow<List<Habit>> {
        return repository.searchHabits(query)
    }
}
