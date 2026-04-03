package com.apachi.auraskin.data.repository

import com.apachi.auraskin.data.local.dao.HabitDao
import com.apachi.auraskin.data.local.entity.HabitCompletionEntity
import com.apachi.auraskin.data.local.entity.HabitEntity
import com.apachi.auraskin.domain.model.Habit
import com.apachi.auraskin.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

import com.apachi.auraskin.data.analytics.AnalyticsManager


class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val analyticsManager: AnalyticsManager
) : HabitRepository {

    override fun getAllHabits(): Flow<List<Habit>> {
        return habitDao.getAllHabits().map { entities ->
            entities.map { entity ->
                Habit(
                    id = entity.id,
                    name = entity.name,
                    streak = entity.streak,
                    createdTimestamp = entity.createdTimestamp
                )
            }
        }
    }

    override suspend fun addHabit(name: String, initialStreak: Int) {
        val habit = HabitEntity(
            id = UUID.randomUUID().toString(),
            name = name,
            streak = initialStreak,
            createdTimestamp = System.currentTimeMillis()
        )
        habitDao.insertHabit(habit)
    }

    override suspend fun incrementHabitStreak(id: String) {
        habitDao.incrementStreak(id)
        habitDao.insertCompletion(
            HabitCompletionEntity(
                habitId = id,
                completionTimestamp = System.currentTimeMillis()
            )
        )
        analyticsManager.logEvent("habit_completed", mapOf("habit_id" to id))
    }

    override suspend fun deleteHabit(id: String) {
        habitDao.deleteHabit(id)
    }

    override suspend fun deleteAllHabits() {
        habitDao.deleteAllCompletions()
        habitDao.deleteAllHabits()
    }

    override fun getCompletions(habitId: String): Flow<List<Long>> {
        return habitDao.getCompletions(habitId).map { list ->
            list.map { it.completionTimestamp }
        }
    }
}
