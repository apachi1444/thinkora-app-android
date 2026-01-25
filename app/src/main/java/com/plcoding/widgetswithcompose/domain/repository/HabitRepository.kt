package com.plcoding.widgetswithcompose.domain.repository

import com.plcoding.widgetswithcompose.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getAllHabits(): Flow<List<Habit>>
    suspend fun addHabit(name: String, initialStreak: Int)
    suspend fun incrementHabitStreak(id: String)
}
