package com.apachi.auraskin.domain.repository

import com.apachi.auraskin.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun addHabit(name: String, initialStreak: Int)
    suspend fun incrementHabitStreak(id: String)
    suspend fun deleteHabit(id: String)
    suspend fun deleteAllHabits()
    fun getCompletions(habitId: String): Flow<List<Long>>
}
