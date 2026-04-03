package com.apachi.auraskin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apachi.auraskin.data.local.dao.QuoteDao
import com.apachi.auraskin.data.local.dao.ReadHistoryDao
import com.apachi.auraskin.data.local.entity.QuoteEntity
import com.apachi.auraskin.data.local.entity.ReadHistoryEntity
import com.apachi.auraskin.data.local.dao.SkinLogDao

@Database(
    entities = [
        QuoteEntity::class, 
        ReadHistoryEntity::class, 
        com.apachi.auraskin.data.local.entity.HabitEntity::class, 
        com.apachi.auraskin.data.local.entity.NotificationEntity::class,
        com.apachi.auraskin.data.local.entity.AchievementEntity::class,
        com.apachi.auraskin.data.local.entity.HabitCompletionEntity::class,
        com.apachi.auraskin.data.local.entity.SkinLogEntity::class
    ],
    version = 7,
    exportSchema = false
)
@androidx.room.TypeConverters(Converters::class)
abstract class AuraDatabase : RoomDatabase() {
    abstract val quoteDao: QuoteDao
    abstract val readHistoryDao: ReadHistoryDao
    abstract val habitDao: com.apachi.auraskin.data.local.dao.HabitDao
    abstract val notificationDao: com.apachi.auraskin.data.local.dao.NotificationDao
    abstract val achievementDao: com.apachi.auraskin.data.local.dao.AchievementDao
    abstract val skinLogDao: SkinLogDao
}
