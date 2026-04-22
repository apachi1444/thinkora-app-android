package com.apachi.auraskin.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apachi.auraskin.data.local.entity.HabitEntity
import com.apachi.auraskin.data.local.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Query("UPDATE habits SET streak = streak + 1 WHERE id = :id")
    suspend fun incrementStreak(id: String)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletion(completion: HabitCompletionEntity)

    @Query("SELECT * FROM habit_completions WHERE habitId = :habitId")
    fun getCompletions(habitId: String): Flow<List<HabitCompletionEntity>>

    @Query("SELECT * FROM habit_completions WHERE completionTimestamp >= :startTime")
    fun getAllCompletionsSince(startTime: Long): Flow<List<HabitCompletionEntity>>

    @Query("DELETE FROM habit_completions")
    suspend fun deleteAllCompletions()

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()
    @Query("SELECT * FROM habits WHERE name LIKE '%' || :query || '%'")
    fun searchHabits(query: String): Flow<List<HabitEntity>>
}
