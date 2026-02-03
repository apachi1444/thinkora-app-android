package com.apachi.thinkora.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apachi.thinkora.data.local.dao.QuoteDao
import com.apachi.thinkora.data.local.dao.ReadHistoryDao
import com.apachi.thinkora.data.local.entity.QuoteEntity
import com.apachi.thinkora.data.local.entity.ReadHistoryEntity

@Database(
    entities = [QuoteEntity::class, ReadHistoryEntity::class, com.apachi.thinkora.data.local.entity.HabitEntity::class, com.apachi.thinkora.data.local.entity.NotificationEntity::class],
    version = 4,
    exportSchema = false
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract val quoteDao: QuoteDao
    abstract val readHistoryDao: ReadHistoryDao
    abstract val habitDao: com.apachi.thinkora.data.local.dao.HabitDao
    abstract val notificationDao: com.apachi.thinkora.data.local.dao.NotificationDao
}
