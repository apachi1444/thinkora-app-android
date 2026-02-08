package com.apachi.thinkora.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.apachi.thinkora.data.local.entity.AchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    fun getAchievements(): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE isUnlocked = 1")
    fun getUnlockedAchievements(): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<AchievementEntity>)

    @Update
    suspend fun update(achievement: AchievementEntity)
    
    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :date WHERE id = :id")
    suspend fun unlockAchievement(id: String, date: Long)
}
