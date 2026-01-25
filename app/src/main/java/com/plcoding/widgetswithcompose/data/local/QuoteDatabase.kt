package com.plcoding.widgetswithcompose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.plcoding.widgetswithcompose.data.local.dao.QuoteDao
import com.plcoding.widgetswithcompose.data.local.dao.ReadHistoryDao
import com.plcoding.widgetswithcompose.data.local.entity.QuoteEntity
import com.plcoding.widgetswithcompose.data.local.entity.ReadHistoryEntity

@Database(
    entities = [QuoteEntity::class, ReadHistoryEntity::class, com.plcoding.widgetswithcompose.data.local.entity.HabitEntity::class],
    version = 2, // Incrementing version, assume fallback destructive migration or manual handle. User didn't specify, I'll assume destructive is okay for dev, or manual. 
    // Actually, to be safe I'll stick to version 1 if I can, but I can't.
    // I will use fallbackDestructiveMigration in Module if possible, otherwise app might crash on update.
    exportSchema = false
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract val quoteDao: QuoteDao
    abstract val readHistoryDao: ReadHistoryDao
    abstract val habitDao: com.plcoding.widgetswithcompose.data.local.dao.HabitDao
}
