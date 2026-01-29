package com.apachi.thinkora.data.repository

import com.apachi.thinkora.data.local.dao.HabitDao
import com.apachi.thinkora.data.local.entity.HabitEntity
import com.apachi.thinkora.domain.model.Habit
import com.apachi.thinkora.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
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
    }

    override suspend fun deleteHabit(id: String) {
        habitDao.deleteHabit(id)
    }
}
