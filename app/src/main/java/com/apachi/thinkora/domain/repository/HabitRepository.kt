package com.apachi.thinkora.domain.repository

import com.apachi.thinkora.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun addHabit(name: String, initialStreak: Int)
    suspend fun incrementHabitStreak(id: String)
    suspend fun deleteHabit(id: String)
}
